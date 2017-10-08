package com.example.okano.trippic

import android.app.VoiceInteractor
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.media.ImageReader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.okano.trippic.DB.DBManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.location.Location
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var latLngList : List<LatLng>? = null
    private lateinit var mMap: GoogleMap
//    var db : SQLiteDatabase? = null
    var trip = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val helper = DBManager(this).getInstance(this)
//        db = helper.writableDatabase

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var route: List<LatLng> = listOf(LatLng(28.0, 120.0), LatLng(15.0, 100.0),
                                            LatLng(10.0, 98.0), LatLng(40.0, 130.0))

        var title: List<String> = listOf("a", "b", "c", "d")
        for (i in 0..3){
            mMap.addMarker(MarkerOptions().position(route[i]).title(title[i]))
        }
        // Add a marker in Sydney and move the camera
//        var c = db!!.query("Trip",arrayOf("id","name","latitude","longitude"),null, null,null, null ,null)
//        if(c.moveToFirst()) {
//        do {
//                var mark = LatLng(c.getDouble(2), c.getDouble(3))
//                mMap.addMarker(MarkerOptions().position(mark).title(c.getString(1)))
//            }while (c.moveToNext())
//       }
        mMap.setOnMarkerClickListener(this)
        drawRoute(route)
        /*
        val mKansai = LatLng(34.435912, 135.243496)
        val mHnl = LatLng(21.318701, -157.921997)
        val mKka = LatLng(45.0,111.0)
        val mTen = LatLng(10.0,13.0)
        val list :List<LatLng> = mutableListOf(mKansai,mHnl,mKka,mTen)
        drawRoute(list)
        */
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
//        var c = db!!.query("Trip",arrayOf("id"),"name = 'test'", null,null, null ,null)
        val arr2: Array<String> = arrayOf("latitude", "longitude","pic")
        val where:Array<String> = arrayOf("")
//        if(c.moveToNext()) {
//            c = db!!.query("Point", arr2, "tripId = 1", null, null, null, "id ASC")
            if (trip) {
//                var c = db!!.query("Trip", arrayOf("Id"), "name = 'test'", null, null, null, null)
                val arr2: Array<String> = arrayOf("latitude", "longitude", "pic")
                /*val where:Array<String> = arrayOf("")
            if(c.moveToNext()) {
                c = db!!.query("Point", arr2, "tripId = 1", null, null, null, "id ASC")
            }
            while (c.moveToNext()){
                var a = c.getFloat(0)
                var b = c.getFloat(1)
                var d = c.getString(2)
            }*/
                trip = false
            } else {
                val intent = Intent(this, CaptureActivity::class.java)
                startActivity(intent)
            }
//        }
        return true
    }

    private fun drawRoute(route: List<LatLng>) {
        val options : PolylineOptions = PolylineOptions()
        for (latLng in route) {
            options.add(latLng)
        }
        options.color(Color.RED)
        options.width(3f)
        options.geodesic(false)
        mMap.addPolyline(options)
    }

}

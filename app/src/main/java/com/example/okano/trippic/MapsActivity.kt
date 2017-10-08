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
    var mMarker: Marker? = null

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
        mMarker = mMap.addMarker(MarkerOptions().position(LatLng(35.6, 139.76)).title("Kyoto Trip"))
        // Add a marker in Sydney and move the camera
//        var c = db!!.query("Trip",arrayOf("id","name","latitude","longitude"),null, null,null, null ,null)
//        if(c.moveToFirst()) {
//        do {
//                var mark = LatLng(c.getDouble(2), c.getDouble(3))
//                mMap.addMarker(MarkerOptions().position(mark).title(c.getString(1)))
//            }while (c.moveToNext())
//       }
        mMap.setOnMarkerClickListener(this)
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
                mMarker?.remove()
                var route: List<LatLng> =
                        listOf(LatLng(35.2, 139.6), LatLng(35.1, 139.5), LatLng(35.1, 139.2), LatLng(35.0, 138.8),
                                LatLng(35.0, 138.5), LatLng(35.0, 138.2), LatLng(35.0, 137.6), LatLng(35.0, 137.3),
                                LatLng(35.0, 137.0), LatLng(35.0, 136.7), LatLng(35.0, 136.3), LatLng(35.1, 136.1),
                                LatLng(35.1, 135.8), LatLng(35.1, 135.7))
                var title: List<String> = listOf("a", "b", "c", "d")
                drawRoute(route)
                mMap.addMarker(MarkerOptions().position(route[2]).title(title[0]))
                mMap.addMarker(MarkerOptions().position(route[5]).title(title[1]))
                mMap.addMarker(MarkerOptions().position(route[7]).title(title[2]))
                mMap.addMarker(MarkerOptions().position(route[13]).title(title[3]))
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

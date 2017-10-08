package com.example.okano.trippic

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.okano.trippic.DB.DBManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.location.Location
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    var db : SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val helper = DBManager(this).getInstance(this)
        db = helper.writableDatabase

        /*var location1 : Locatio
        writeLine()*/
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

        // Add a marker in Sydney and move the camera
        var c = db!!.query("Trip",arrayOf("id","name","latitude","longitude"),null, null,null, null ,null)
        if(c.moveToFirst()) {
            do {
                var mark = LatLng(c.getDouble(2), c.getDouble(3))
                mMap.addMarker(MarkerOptions().position(mark).title(c.getString(1)))
            }while (c.moveToNext())
        }
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        var c = db!!.query("Trip",arrayOf("id"),"name = 'test'", null,null, null ,null)
        val arr2: Array<String> = arrayOf("latitude", "longitude","pic")
        val intent = Intent(this, LogsActivity::class.java)
        intent.putExtra("tripId", c.getInt(0))
        startActivity(intent)
        /*val where:Array<String> = arrayOf("")
        if(c.moveToNext()) {
            c = db!!.query("Point", arr2, "tripId = 1", null, null, null, "id ASC")
        }
        while (c.moveToNext()){
            var a = c.getFloat(0)
            var b = c.getFloat(1)
            var d = c.getString(2)
        }*/
        return false
    }

    fun writeLine(locationStart : Location?,locationEnd : Location?) {
        if(locationStart==null||locationEnd==null){
            return
        }
        val start = LatLng(locationStart.latitude, locationStart.longitude)
        val end = LatLng(locationEnd.latitude, locationEnd.longitude)
        val straight : PolylineOptions =  PolylineOptions().add(start, end).geodesic(false).color(Color.RED).width(3f)
        mMap.addPolyline(straight)
    }

}

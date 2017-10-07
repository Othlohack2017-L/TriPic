package com.example.okano.trippic.gpsCoordination

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log


/**
 * Created by yusuke on 2017/10/07.
 */
class GPSCoordination : LocationListener{
    private val locationManager : LocationManager? = null

    constructor(){
        Log.d("debug","locationStart()")
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,50f,this)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(location: Location?) {
        //To change body of created functions use File | Settings | File Templates.
        Log.d("mytag",""+location?.longitude+":"+location?.latitude)
    }

}

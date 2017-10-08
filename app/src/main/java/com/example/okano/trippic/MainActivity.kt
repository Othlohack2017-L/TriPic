package com.example.okano.trippic

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import android.graphics.Bitmap

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.okano.trippic.DB.DBManager
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),LocationListener {
    var eventname = "event"
    var db : SQLiteDatabase? = null
    var text : TextView? = null
    var _imageUri: Uri? = null
    var locationManager : LocationManager? = null
    var id : Int? = null
    var locationRotation : Location? = null
    var minuteRotation : Double = 0.0
    var recordFlog : Boolean = false
    var nearLocation : Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helper = DBManager(this).getInstance(this)
        db = helper.writableDatabase

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1000)
        }
        else{
            locationStart()
        }

        nearLocation = null
    }

    fun locationStart(){
        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val gpsEnable : Boolean = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!gpsEnable){
            var settingIntent : Intent = Settings.ACTION_LOCATION_SOURCE_SETTINGS as Intent
            startActivity(settingIntent)
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0f,this)
    }

    fun changeActivity(view: View){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun startLog(view: View){
        val layout = LinearLayout(this)
        val eventBox = EditText(this)
        eventBox.setHint("イベント名")
        layout.addView(eventBox)
        AlertDialog.Builder(this)
                .setTitle("Trip name")
                .setMessage("旅行の名前を入力してください")
                .setView(layout)
                .setPositiveButton("OK", object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface, which: Int){
                        eventname = eventBox.text.toString()
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show()

        val insertValue = ContentValues()
        insertValue.put("name",eventname)
        insertValue.put("startTime",getNowTime() )
        insertValue.put("latitude",80.0)
        insertValue.put("longitude",80.0)
        id = db!!.insert("Trip",eventname, insertValue).toInt()

        var i = 0
        while(2 > i){
            i++
            val test = ContentValues()
            test.put("latitude", (i*2).toFloat())
            test.put("longitude", (i*2).toFloat())
            test.put("pic", "")
            test.put("tripId",id)
            db!!.insert("Point",null,test)
        }
        /*val arr: Array<String> = arrayOf("name", "startTime")
        val c = db!!.query("Trip",arr,null,null,null, null ,null)
        if(c.moveToNext()){

        }*/


    }

    fun endLog(view: View) {
        if (id != null) {
            val updateValue = ContentValues()
            updateValue.put("endTime", getNowTime())
            db!!.update("Trip", updateValue, "id=?", arrayOf(id.toString()))
            id = null
        }
    }

    fun launchCamera(view: View){
        //クリック時の処理
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions, 2000)
            return
        }
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")  // （1）
        val now = Date(System.currentTimeMillis())  // （1）
        val nowStr = dateFormat.format(now)  // （1）
        val fileName = "UseCameraActivityPhoto_$nowStr.jpg"  // （1）

        val values = ContentValues()  // （2）//ファイル名設定
        values.put(MediaStore.Images.Media.TITLE, fileName)  // （3）putでデータを格納
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")  // （4）

        val resolver = contentResolver  // （5）
        _imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)  // （6）新しいデータの格納先を確保しこれが一つ目、それを表すUriオブジェクトを生成してくれる

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)  // （7）
   //     intent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider)  // （8）
        startActivityForResult(intent, 200)  // （9）
//        setResult(Activity.RESULT_OK, intent)
    }

    fun getNowTime(): String {
        var calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = calendar.get(Calendar.MONTH).toString()
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val hour = calendar.get(Calendar.HOUR_OF_DAY).toString()
        val minute = calendar.get(Calendar.MINUTE).toString()
        val second = calendar.get(Calendar.SECOND).toString()
        return year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second
    }

    fun startRecord(){
        minuteRotation = 0.0
        recordFlog = true
    }

    fun endRecord(){
        recordFlog = false
    }

    fun takePicture(): Location? {
        return nearLocation
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val ivCamera = findViewById<ImageView>(R.id.ivCamera)
            launchCamera(ivCamera)//パーミッションが許可されたときにもう一度onCameraImageClick()を呼び出す
        }
        if(requestCode==1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationStart()
                return
            }
            else{
                var toast : Toast = Toast.makeText(this,"現在位置は使用できません",Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {//戻ってきたときの処理　requestodeは識別子C resultCodeは結果を表す Intentに何か問題あり
       // super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {  // （1）
            val ivCamera = findViewById<ImageView>(R.id.ivCamera)  // その画像をImageViewに適用
            val text=data.data.toString() as String
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        }
    }

    /*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val ivCamera = findViewById<ImageView>(R.id.ivCamera)
           launchCamera(ivCamera)//パーミッションが許可されたときにもう一度onCameraImageClick()を呼び出す
        }
    }
    */

    override fun onLocationChanged(location: Location?) {
        //if(recordFlog) {
            if (Calendar.MINUTE - minuteRotation > 1.0) {
                Log.d("mytag", "" + location?.getLatitude() + ":" + location?.getLongitude())
                locationRotation = location
                minuteRotation = Calendar.MINUTE.toDouble()
            }
            //nearLocation = location
        //}
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
}
package com.example.okano.trippic


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.okano.trippic.DB.DBManager
import java.text.SimpleDateFormat
import com.example.okano.trippic.gpsCoordination.GPSCoordination
import java.util.*

class MainActivity : AppCompatActivity() {

    var eventname = "event"
    var db : SQLiteDatabase? = null
    var text : TextView? = null
    var _imageUri: Uri? = null
    //var gpscoordination:GPSCoordination? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helper = DBManager(this).getInstance(this)
        db = helper.writableDatabase
        //gpscoordination = GPSCoordination()

    }

    fun changeActivity(view: View){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun startLog(view: View){

        AlertDialog.Builder(this)
                .setTitle("Trip name")
                .setMessage("旅行の名前を入力してください")
                .setPositiveButton("OK", object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface, which: Int){

                    }
                })
                .setNegativeButton("CANCEL", null)
                .show()

        val insertValue = ContentValues()
        insertValue.put("name","test")
        insertValue.put("startTime",getNowTime() )
        insertValue.put("latitude",2.5)
        insertValue.put("longitude",2.5)
        db!!.insert("Trip",eventname, insertValue)

        var i = 0
        while(2 > i){
            i++
            val test = ContentValues()
            test.put("latitude", i.toFloat())
            test.put("longitude", i.toFloat())
            test.put("pic", "")
            test.put("tripId",1)
            db!!.insert("Point",null,test)
        }
        /*val arr: Array<String> = arrayOf("name", "startTime")
        val c = db!!.query("Trip",arr,null,null,null, null ,null)
        if(c.moveToNext()){

        }*/
    }

    fun endLog(view: View){

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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri)  // （8）
        startActivityForResult(intent, 200)  // （9）
    }

    fun getLocation(){

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {//戻ってきたときの処理　requestodeは識別子C resultCodeは結果を表す
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {  // （1）
            //Bitmap bitmap =  data.getParcelableExtra("data");  // （2）カメラアプリが撮影した画像を取得カメラアプリの画像は勝手にインテントに入ってる
            //val ivCamera = findViewById(R.id.ivCamera) as ImageView  // その画像をImageViewに適用
            //ivCamera.setImageURI(_imageUri)  // （3）
            Toast.makeText(this@MainActivity, "+_imageUri+", Toast.LENGTH_SHORT).show()
        }
    }

}
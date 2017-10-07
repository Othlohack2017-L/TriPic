package com.example.okano.trippic


import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import com.example.okano.trippic.DB.DBManager
import java.util.*

class MainActivity : AppCompatActivity() {

    var eventname = "event"
    var db : SQLiteDatabase? = null
    var text : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helper = DBManager(this)
        db = helper.writableDatabase


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
        insertValue.put("name",eventname)
        insertValue.put("startTime",getNowTime() )
        db!!.insert("Trip",eventname, insertValue)
        val arr: Array<String> = arrayOf("name", "startTime")
        val c = db!!.query("Trip",arr,null,null,null, null ,null)
        if(c.moveToNext()){

        }
    }

    fun endLog(view: View){

    }

    fun launchCamera(view: View){

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

}
package com.example.okano.trippic

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.okano.trippic.DB.DBManager

class MainActivity : AppCompatActivity() {

    var eventname = "event"
    var db : SQLiteDatabase? = null
    var text : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helper = DBManager(this)
        db = helper.writableDatabase

        text = findViewById(R.id.text)

    }

    fun changeActivity(view: View){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun startLog(view: View){
        val insertValue = ContentValues()
        insertValue.put("name",eventname)
        db!!.insert("Trip",eventname, insertValue)
        val arr: Array<String> = arrayOf("name")
        val c = db!!.query("Trip",arr,null,null,null, null ,null)
        if(c.moveToNext()){
            text!!.setText(c.getString(0))
        }
    }

    fun endLog(view: View){

    }

    fun launchCamera(view: View){

    }

    fun getLocation(){

    }

}
package com.example.okano.trippic

import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    var eventname = "event"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
    }

    fun endLog(view: View){

    }

    fun launchCamera(view: View){

    }

    fun getLocation(){

    }

}
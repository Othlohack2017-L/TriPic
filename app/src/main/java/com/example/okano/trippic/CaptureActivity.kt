package com.example.okano.trippic

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_capture.view.*
import java.io.IOException

class CaptureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        val imageView = findViewById<ImageView>(R.id.image_view)
        try{
            val istream = getResources().getAssets().open("gazou_187.jpg")
            val bitmap = BitmapFactory.decodeStream(istream)
            imageView.setImageBitmap(bitmap)
        }catch(e: IOException){
            Log.d("Assets", "Error")
        }
    }


}

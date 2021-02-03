package com.example.assistant
//
//  MainActivity.kt
//  Assistant
//
//  Created by Leonid Spiridonov on 03.02.2021.
//
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMain.setOnClickListener {
            checkPermission()
            startService(Intent(application, MyService::class.java))
        }

        btnCancel.setOnClickListener {
            stopService(Intent(application, MyService()::class.java))
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Пожалуйста, дайте разрешение приложению!", Toast.LENGTH_LONG).show()
                val REQUEST_CODE = 101
                val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                myIntent.data = Uri.parse("package:$packageName")
                startActivityForResult(myIntent, REQUEST_CODE)
            }
        }
    }
}
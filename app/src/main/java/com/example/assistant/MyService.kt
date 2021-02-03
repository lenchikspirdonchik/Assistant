package com.example.assistant

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast


class MyService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var image: ImageView
    private lateinit var params: WindowManager.LayoutParams


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        image = ImageView(this)
        image.setImageDrawable(getDrawable(R.drawable.pied_piper))


        val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
                200,
                200,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP
        params.x = 0
        params.y = 100



        image.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var shouldClick = false
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        shouldClick = true
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (shouldClick) {
                            Toast.makeText(
                                    applicationContext,
                                    "Клик по тосту случился!",
                                    Toast.LENGTH_LONG
                            ).show()
                            params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN
                            windowManager.updateViewLayout(image, params)
                            shouldClick = false
                        }
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = (initialX
                                + (event.rawX - initialTouchX).toInt())
                        params.y = (initialY
                                + (event.rawY - initialTouchY).toInt())
                        windowManager.updateViewLayout(image, params)
                        shouldClick = false
                        return true
                    }
                }
                return false
            }
        })
        windowManager.addView(image, params)
    }


    private fun doSomething() {
        Toast.makeText(baseContext, "did something", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(image)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
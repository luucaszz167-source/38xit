package com.thirtyeightxhx.panel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

class FloatingService : Service() {
    private lateinit var windowManager: WindowManager
    private var panel: View? = null
    private var espDemoView: EspDemoView? = null
    private var espEnabled = false
    private lateinit var params: WindowManager.LayoutParams

    override fun onCreate() {
        super.onCreate()
        startForeground(38, createNotification())
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        showPanel()
    }

    private fun createNotification(): Notification {
        val channelId = "38hx_panel"
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(NotificationChannel(
                channelId, "38HX Panel", NotificationManager.IMPORTANCE_LOW
            ))
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
                .setContentTitle("38HX Panel ativo")
                .setContentText("Toque em parar para fechar o painel.")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setContentTitle("38HX Panel ativo")
                .setContentText("Painel flutuante em execução.")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .build()
        }
    }

    private fun showPanel() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 14, 18, 14)
            setBackgroundColor(Color.rgb(9, 17, 32))
        }

        val header = TextView(this).apply {
            text = "38HX  •  PAINEL     [ arraste aqui ]"
            textSize = 14f
            setTextColor(Color.rgb(75, 174, 255))
            setPadding(4, 4, 4, 12)
        }
        header.setOnTouchListener(DragListener())

        fun label(text: String): TextView = TextView(this).apply {
            this.text = text
            textSize = 13f
            setTextColor(Color.WHITE)
            setPadding(2, 8, 2, 2)
        }

        root.addView(header)
        root.addView(label("Ajuste demonstrativo de mira"))
        root.addView(makeSlider())
        root.addView(label("Recursos visuais"))
        root.addView(makeEspToggle())
        root.addView(makeToggle("Mira personalizada"))
        root.addView(makeToggle("Indicador de FPS (visual)"))

        val close = TextView(this).apply {
            text = "FECHAR PAINEL"
            textSize = 13f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(20, 92, 160))
            setPadding(12, 12, 12, 12)
            setOnClickListener { stopSelf() }
        }
        root.addView(close)

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 24
            y = 160
        }
        panel = root
        windowManager.addView(root, params)
    }

    private fun makeEspToggle(): TextView = TextView(this).apply {
        text = "○  ESP DE TESTE — jogador fictício"
        textSize = 13f
        setTextColor(Color.rgb(205, 220, 240))
        setPadding(4, 10, 4, 10)
        setOnClickListener {
            espEnabled = !espEnabled
            if (espEnabled) showDemoMarker() else removeDemoMarker()
            text = (if (espEnabled) "●  " else "○  ") + "ESP DE TESTE — jogador fictício"
            setTextColor(if (espEnabled) Color.rgb(75, 174, 255) else Color.rgb(205, 220, 240))
        }
    }

    private fun showDemoMarker() {
        if (espDemoView != null) return
        val marker = EspDemoView(this)
        val overlayType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE

        val overlayParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            overlayType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        windowManager.addView(marker, overlayParams)
        espDemoView = marker
    }

    private fun removeDemoMarker() {
        espDemoView?.let { windowManager.removeView(it) }
        espDemoView = null
    }

    private fun makeSlider(): SeekBar = SeekBar(this).apply {
        max = 100
        progress = 50
    }

    private fun makeToggle(title: String): TextView = TextView(this).apply {
        text = "○  $title"
        textSize = 13f
        setTextColor(Color.rgb(205, 220, 240))
        setPadding(4, 10, 4, 10)
        setOnClickListener {
            val enabled = tag != true
            tag = enabled
            text = (if (enabled) "●  " else "○  ") + title
            setTextColor(if (enabled) Color.rgb(75, 174, 255) else Color.rgb(205, 220, 240))
        }
    }

    private inner class DragListener : View.OnTouchListener {
        private var startX = 0
        private var startY = 0
        private var touchX = 0f
        private var touchY = 0f

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = params.x
                    startY = params.y
                    touchX = event.rawX
                    touchY = event.rawY
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = startX + (event.rawX - touchX).toInt()
                    params.y = startY + (event.rawY - touchY).toInt()
                    panel?.let { windowManager.updateViewLayout(it, params) }
                    return true
                }
            }
            return true
        }
    }

    override fun onDestroy() {
        removeDemoMarker()
        panel?.let { windowManager.removeView(it) }
        panel = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

package com.thirtyeightxhx.panel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 48, 36, 36)
            setBackgroundColor(0xFF080D18.toInt())
        }
        val title = TextView(this).apply {
            text = "38HX PANEL"
            textSize = 28f
            setTextColor(0xFF42A5FF.toInt())
        }
        val subtitle = TextView(this).apply {
            text = "Painel flutuante demonstrativo"
            textSize = 14f
            setTextColor(0xFFB8C7D9.toInt())
            setPadding(0, 8, 0, 28)
        }
        val permission = Button(this).apply {
            text = "CONCEDER SOBREPOSIÇÃO"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")))
            }
        }
        val start = Button(this).apply {
            text = "ABRIR PAINEL FLUTUANTE"
            setOnClickListener {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startService(Intent(this@MainActivity, FloatingService::class.java))
                    Toast.makeText(this@MainActivity, "Painel iniciado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Conceda a permissão de sobreposição primeiro", Toast.LENGTH_LONG).show()
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")))
                }
            }
        }
        val note = TextView(this).apply {
            text = "Este APK demonstra apenas a interface externa. Os controles não leem nem alteram a memória do Free Fire e não implementam mira automática ou ESP real."
            textSize = 13f
            setTextColor(0xFF91A2B8.toInt())
            setPadding(0, 24, 0, 0)
        }
        layout.addView(title)
        layout.addView(subtitle)
        layout.addView(permission)
        layout.addView(start)
        layout.addView(note)
        setContentView(layout)
    }
}

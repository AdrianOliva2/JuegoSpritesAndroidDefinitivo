package com.example.juegosprites

import android.os.Bundle
import android.view.View
import com.example.juegosprites.activity.JuegoActivity
import com.example.juegosprites.activity.PortraitActivity
import com.example.juegosprites.activity.PuntosActivity

class MainActivity : PortraitActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnJugar = findViewById<android.widget.Button>(R.id.btnJugar)
        val btnPuntos = findViewById<android.widget.Button>(R.id.btnPuntos)
        btnJugar.setOnClickListener(this)
        btnPuntos.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnJugar -> {
                val intent = android.content.Intent(this, JuegoActivity::class.java)
                startActivity(intent)
            }
            R.id.btnPuntos -> {
                val intent = android.content.Intent(this, PuntosActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
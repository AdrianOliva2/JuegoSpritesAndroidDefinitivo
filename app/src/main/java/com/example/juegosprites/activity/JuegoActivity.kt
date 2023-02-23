package com.example.juegosprites.activity

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.juegosprites.R

class JuegoActivity : PortraitActivity() {

    lateinit var music: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(JuegoSurface(this, applicationContext))

        music = MediaPlayer.create(this, R.raw.playing)
        music.setVolume(1f, 1f)
        music.start()
        music.isLooping = true
    }

    override fun onBackInvoked() {
        music.stop()
        super.onBackInvoked()
    }

    fun dialogWin(puntos: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
            .setTitle("¡Has ganado!. Has conseguido: $puntos puntos.")
        builder.setPositiveButton("OK") { _, _ ->
            val sharedPreferences =
                getSharedPreferences("puntuaciones", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val primero = sharedPreferences.getInt("primero", 0)
            val segundo = sharedPreferences.getInt("segundo", 0)
            val tercero = sharedPreferences.getInt("tercero", 0)
            if (puntos > primero) {
                editor.putInt("primero", puntos)
                editor.putInt("segundo", primero)
                editor.putInt("tercero", segundo)
                editor.apply()
            } else if (puntos > segundo) {
                editor.putInt("segundo", puntos)
                editor.putInt("tercero", segundo)
                editor.apply()
            } else if (puntos > tercero) {
                editor.putInt("tercero", puntos)
                editor.apply()
            }
            music.stop()
            finish()
            onBackInvoked()
        }
        val dialogA = builder.create()
        dialogA.setView(dialogA.layoutInflater.inflate(R.layout.msglay, null))
        dialogA.show()
    }

    fun dialogLose() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
            .setTitle("¡Perdiste! Has pegado a tu amigo Nobita. Has conseguido: 0 puntos.")
        builder.setPositiveButton("OK") { _, _ ->
            music.stop()
            finish()
            onBackPressed()
        }
        val dialogA = builder.create()
        dialogA.setView(dialogA.layoutInflater.inflate(R.layout.msglay, null))
        dialogA.show()
    }
}
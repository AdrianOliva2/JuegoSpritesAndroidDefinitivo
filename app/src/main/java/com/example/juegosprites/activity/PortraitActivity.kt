package com.example.juegosprites.activity

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity

open class PortraitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar!!.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                onBackInvoked()
            }
        }
    }

    protected open fun onBackInvoked() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setTitle("Salir")
        alert.setMessage("¿Desea salir de la aplicación?")
        alert.setPositiveButton("Si") { _, _ ->
            finish()
        }
        alert.setNegativeButton("No") { _, _ -> }
        alert.show()
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)",
        "android",
        "android"
    )
    )
    override fun onBackPressed() {
        onBackInvoked()
    }

}
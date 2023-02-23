package com.example.juegosprites.model

import android.graphics.Canvas
import android.view.SurfaceHolder
import com.example.juegosprites.activity.JuegoSurface

class DrawThread(private val holder: SurfaceHolder, private val juegoSurface: JuegoSurface): Thread() {

    private var run = false
    companion object {
        private const val FPS: Long = 20
    }

    fun setRunning(run: Boolean) {
        this.run = run
    }

    override fun run() {
        var cv: Canvas?
        val tickPS: Long = 1000 / FPS
        var startTime: Long
        var sleepTime: Long

        while (run) {
            cv = null
            startTime = System.currentTimeMillis()
            try {
                cv = this.holder.lockCanvas(null)
                if (cv != null) {
                    synchronized(this.holder) { this.juegoSurface.postInvalidate() }
                }
            } finally {
                if (cv != null) this.holder.unlockCanvasAndPost(cv)
            }
            sleepTime = tickPS - (System.currentTimeMillis() - startTime)
            try {
                if (sleepTime > 0) sleep(sleepTime)
                else sleep(10)
            } catch (e: Exception) {

            }
        }
    }

}

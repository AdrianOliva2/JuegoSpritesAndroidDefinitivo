package com.example.juegosprites.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.example.juegosprites.activity.JuegoSurface
import kotlin.math.atan2
import kotlin.math.round

class Jugador(private var surface: JuegoSurface, private var bmp: Bitmap) {

    var with = bmp.width / COLS
    var height = bmp.height / ROWS
    var xSpeed = 0
    var ySpeed = 0
    private var x: Int = surface.width / 2
    private var y: Int = surface.height / 2
    private var currentFrame: Int = 0
    private val DIRECTION: IntArray = intArrayOf(3, 1, 0, 2)

    companion object {
        private const val ROWS: Int = 4
        private const val COLS: Int = 3
    }

    private fun getAnimationRow(): Int {
        val dirDouble = atan2(xSpeed.toDouble(), ySpeed.toDouble()) / (Math.PI / 2) + 2
        val direction: Int = (round(dirDouble) % ROWS).toInt()
        return DIRECTION[direction]
    }

    fun isCollition(x: Int, y: Int): Boolean {
        return x > this.x && x < this.x + with && y > this.y && y < this.y + height
    }

    private fun update() {
        if (x > surface.width - with - xSpeed || x + xSpeed < 0) xSpeed = 0

        if (y > surface.height - height - ySpeed || y + ySpeed < 0) ySpeed = 0

        x += xSpeed
        y += ySpeed

        currentFrame = ++currentFrame % COLS
    }

    fun onDraw(c: Canvas) {
        update()
        val srcX: Int = currentFrame * with
        val srcY: Int = getAnimationRow() * height
        val src = Rect(srcX, srcY, srcX + with, srcY + height)
        val dst = Rect(x, y, x + with, y + height)
        c.drawBitmap(bmp, src, dst, null)
    }

}
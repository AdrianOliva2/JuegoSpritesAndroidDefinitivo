package com.example.juegosprites.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.example.juegosprites.activity.JuegoSurface
import java.util.*
import kotlin.math.atan2
import kotlin.math.roundToInt

class Sprite(sprites: ArrayList<Sprite>, private var juegoSurface: JuegoSurface, bmp: Bitmap, nobita: Boolean, rey: Boolean) {

    private val ROWS = 4
    private val COLUMNS = 3
    private var x = 0
    private var y = 0
    private var xSpeed = 0
    private var ySpeed = 0
    private var bmp: Bitmap? = null
    private var currentFrame = 0
    private var width = 0
    private var heigth = 0
    private val low = -20
    private val high = 21
    private var nobita = false
    private var rey = false
    private var life = 300
    var lista: ArrayList<Sprite>? = null

    var DIRECTION = intArrayOf(3, 1, 0, 2)

    fun isNobita(): Boolean {
        return nobita
    }

    fun isRey(): Boolean {
        return rey
    }

    private fun getAnimationRow(): Int {
        val dirDouble = atan2(xSpeed.toDouble(), ySpeed.toDouble()) / (Math.PI / 2) + 2
        val direccion = dirDouble.roundToInt() % ROWS
        return DIRECTION[direccion]
    }

    fun isCollition(x2: Float, y2: Float): Boolean {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + heigth
    }

    fun getMiddleX(): Float {
        return (x + width / 2).toFloat().roundToInt().toFloat()
    }

    fun getMiddleY(): Float {
        return (y + heigth / 2).toFloat().roundToInt().toFloat()
    }

    private fun update() {
        if (x > juegoSurface.width - width - xSpeed || x + xSpeed < 0) xSpeed = -xSpeed
        x += xSpeed
        if (y > juegoSurface.height / 5 * 4 - heigth - ySpeed || y + ySpeed < 0) ySpeed = -ySpeed
        y += ySpeed
        currentFrame = ++currentFrame % COLUMNS
        if (nobita) {
            synchronized(juegoSurface.holder) {
                if (--life < 1) {
                    lista!!.remove(this)
                    juegoSurface.nobActivo = false
                }
            }
        }
    }

    fun onDraw(cv: Canvas) {
        update()
        val srcX = currentFrame * width
        val srcY = getAnimationRow() * heigth
        val src = Rect(srcX, srcY, srcX + width, srcY + heigth)
        val dst = Rect(x, y, x + width, y + heigth)
        cv.drawBitmap(bmp!!, src, dst, null)
    }

}

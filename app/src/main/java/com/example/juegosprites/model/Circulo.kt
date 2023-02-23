package com.example.juegosprites.model

import android.graphics.Canvas
import android.graphics.Paint
import java.lang.Math.pow
import kotlin.math.pow

class Circulo(private var radio: Float, private var x: Float, private var y: Float, private var color: Int): Figura(x, y, color) {

    override fun onDraw(canvas: Canvas) {
        val paint = Paint()
        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(x, y, radio, paint)
    }

    override fun estaDentro(x: Float, y: Float): Boolean {
        val distanciaX = x - this.x
        val distanciaY = y - this.y
        if (radio.toDouble().pow(2.toDouble()) > (distanciaX.toDouble().pow(2.toDouble()) + distanciaY.toDouble().pow(2.toDouble())))
            return true
        return false
    }

}
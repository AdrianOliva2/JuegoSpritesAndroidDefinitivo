package com.example.juegosprites.model

import android.graphics.Canvas

class Rectangulo(var x: Float, var y: Float, var color: Int, fl: Float, transparent: Int): Figura(x, y, color) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun estaDentro(x: Float, y: Float): Boolean {
        return super.estaDentro(x, y)
    }

}
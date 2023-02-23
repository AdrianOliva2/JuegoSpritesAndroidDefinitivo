package com.example.juegosprites.model

import android.graphics.Canvas

open class Figura(private var x: Float, private var y: Float, private var color: Int) {

    open fun onDraw(canvas: Canvas) {}

    open fun estaDentro(x: Float, y: Float): Boolean {
        return false
    }

}
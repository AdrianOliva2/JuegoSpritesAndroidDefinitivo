package com.example.juegosprites.activity

import android.content.Context
import android.graphics.*
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.juegosprites.R
import com.example.juegosprites.model.*
import java.util.*

class JuegoSurface(private val activity: JuegoActivity, context: Context): SurfaceView(context), SurfaceHolder.Callback {

    private var ratasGrandesMuertas: Int = 0
    private lateinit var hilo: DrawThread
    private lateinit var img1: Sprite
    private lateinit var pic: Bitmap
    private var puntos: Int = 0
    private lateinit var tomb: Bitmap
    private lateinit var cruceta: Bitmap
    private lateinit var boton: Bitmap
    private lateinit var mapa: Bitmap
    private lateinit var backpad: Bitmap
    private val sprites: ArrayList<Sprite> = ArrayList()
    private val spritesTemporales: ArrayList<TempSprite> = ArrayList()
    private lateinit var player: Jugador
    private var limiteGamepad: Float = (height/5*4).toFloat()
    private lateinit var top: Rectangulo
    private lateinit var left: Rectangulo
    private lateinit var bottom: Rectangulo
    private lateinit var right: Rectangulo
    private lateinit var mach: Circulo
    private var udX: Float = (width / 12 + 15).toFloat()
    private var udY: Float = (height / 5 / 5).toFloat()
    private val rnd: Random = Random()
    var nobActivo = false
    private lateinit var soundPool: SoundPool
    private lateinit var sm: IntArray

    init {
        setBackgroundColor(Color.WHITE)
        holder.addCallback(this)
        InitSound(context)
    }

    override fun onDraw(canvas: Canvas?) {
        val p = Paint()
        p.style = Paint.Style.FILL
        p.color = Color.WHITE
        canvas?.drawPaint(p)


        canvas?.drawBitmap(mapa, 0F, 0F, p)
        canvas?.drawBitmap(backpad, 0F, limiteGamepad, p)

        p.color = Color.BLACK
        p.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        p.textSize = 60F
        canvas?.drawText("Puntos = $puntos", 30f, 80f, p)
        canvas?.drawText("Reyes = $ratasGrandesMuertas", (width / 3 * 2).toFloat(), 80f, p)
        canvas?.drawBitmap(cruceta, udX, limiteGamepad, p)
        canvas?.drawBitmap(boton, udX * 7, limiteGamepad + udY, p)
        if (canvas != null) {
            mach.onDraw(canvas)
            top.onDraw(canvas)
            bottom.onDraw(canvas)
            right.onDraw(canvas)
            left.onDraw(canvas)
        }


        synchronized(holder) {
            for (i in spritesTemporales.indices.reversed()) {
                spritesTemporales[i].onDraw(canvas)
            }
        }


        synchronized(holder) {
            if (canvas != null)
                player.onDraw(canvas)
            for (i in sprites.indices.reversed()) {
                sprites[i].onDraw(canvas)
            }
        }

        synchronized(holder) {
            if (sprites.size == 3) {
                createSprites()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                if (top.estaDentro(event.x, event.y)) {
                    player.xSpeed = 0
                    player.ySpeed = -21
                } else if (bottom.estaDentro(event.x, event.y)) {
                    player.xSpeed = 0
                    player.ySpeed = 21
                } else if (left.estaDentro(event.x, event.y)) {
                    player.ySpeed = 0
                    player.xSpeed = -21
                } else if (right.estaDentro(event.x, event.y)) {
                    player.ySpeed = 0
                    player.xSpeed = 21
                }
                if (mach.estaDentro(event.x, event.y)) {
                    synchronized(holder) {
                        var i = sprites.size - 1
                        while (i >= 0) {
                            if (player.isCollition(
                                    sprites[i].getMiddleX(),
                                    sprites[i].getMiddleY()
                                )
                            ) {
                                spritesTemporales.add(
                                    TempSprite(
                                        spritesTemporales,
                                        this,
                                        sprites[i].getMiddleX(),
                                        sprites[i].getMiddleY(),
                                        tomb
                                    )
                                )
                                if (!sprites[i].isNobita() && !sprites[i].isRey()) {
                                    puntos += 5
                                    playSound(0)
                                } else if (sprites[i].isRey()) {
                                    puntos += 20
                                    ratasGrandesMuertas++
                                    playSound(0)
                                    if (ratasGrandesMuertas == 3) {
                                        activity.dialogWin(puntos)
                                        activity.music.stop()
                                        playSound(1)
                                    }
                                } else if (sprites[i].isNobita()) {
                                    playSound(2)
                                    activity.music.stop()
                                    activity.dialogLose()
                                }
                                synchronized(holder) { sprites.remove(sprites[i]) }
                                val rnd = Random()
                                val num = rnd.nextInt(11)
                                if (num == 1) {
                                    synchronized(holder) { createKing() }
                                } else if (num == 2 || num == 4 || num == 6) {
                                    synchronized(holder) {
                                        if (!nobActivo) {
                                            createNobita()
                                            nobActivo = true
                                        }
                                    }
                                } else {
                                    synchronized(holder) { createSprites() }
                                }
                                break
                            }
                            i--
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                player.ySpeed = 0
                player.xSpeed = 0
            }
        }
        return true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        hilo = DrawThread(getHolder(), this)
        hilo.setRunning(true)
        hilo.start()
        synchronized(getHolder()) {
            createSprites()
            createSprites()
            createSprites()
            createSprites()
        }
        tomb = BitmapFactory.decodeResource(resources, R.drawable.tomb)
        limiteGamepad = (height / 5 * 4).toFloat()
        udX = (width / 12 + 15).toFloat()
        udY = (height / 5 / 5).toFloat()
        top = Rectangulo(
            udX * 2 + 34,
            limiteGamepad + 21,
            (udY * 2 - 20).toInt(),
            udX + 40,
            Color.TRANSPARENT
        )
        bottom = Rectangulo(
            udX * 2 + 34, limiteGamepad + udY * 3,
            (udY * 2 - 20).toInt(), udX + 40, Color.TRANSPARENT
        )
        left = Rectangulo(
            udX + 19, limiteGamepad + udY * 2 - 28,
            (udY + 54).toInt(), udX + 40, Color.TRANSPARENT
        )
        right = Rectangulo(
            udX * 3 + 47, limiteGamepad + udY * 2 - 28,
            (udY + 54).toInt(), udX + 40, Color.TRANSPARENT
        )

        val cruz = BitmapFactory.decodeResource(resources, R.drawable.pad)
        cruceta = Bitmap.createScaledBitmap(cruz, udX.toInt() * 4, udY.toInt() * 5, true)
        val circulo = BitmapFactory.decodeResource(resources, R.drawable.machbutton)
        boton = Bitmap.createScaledBitmap(circulo, udX.toInt() * 2 + 30, udY.toInt() * 3, true)
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.doramio)
        player = Jugador(this, bmp)
        mach = Circulo(udX * 8 + 15, limiteGamepad + udY + 110, 127F, Color.TRANSPARENT)
        val mapita = BitmapFactory.decodeResource(resources, R.drawable.map)
        mapa = Bitmap.createScaledBitmap(mapita, width, limiteGamepad.toInt(), true)
        val detrasBack = BitmapFactory.decodeResource(resources, R.drawable.backpad)
        backpad = Bitmap.createScaledBitmap(detrasBack, width, height / 5, true)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true

        hilo.setRunning(false)
        while (retry) {
            try {
                hilo.join()
                retry = false
            } catch (e: InterruptedException) {

            }
        }
    }

    private fun createSprite(resource: Int, nobita: Boolean, rey: Boolean): Sprite {
        val bmp = BitmapFactory.decodeResource(resources, resource)
        return Sprite(sprites, this, bmp, nobita, rey)
    }

    private fun createNobita() {
        sprites.add(createSprite(R.drawable.nobita3, true, false))
    }

    private fun createKing() {
        sprites.add(createSprite(R.drawable.fatrats, false, true))
    }

    private fun createSprites() {
        sprites.add(createSprite(R.drawable.rat, false, false))
    }

    private fun InitSound(context: Context) {
        val maxStreams = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = SoundPool.Builder()
                .setMaxStreams(maxStreams)
                .build()
        } else {
            soundPool =
                SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0)
        }

        sm = IntArray(3)
        sm[0] = soundPool.load(context, R.raw.kill, 1)
        sm[1] = soundPool.load(context, R.raw.win, 1)
        sm[2] = soundPool.load(context, R.raw.doramio, 1)
    }

    private fun playSound(sound: Int) {
        soundPool.play(sm[sound], 1F, 1F, 1, 0, 1F)
    }

}
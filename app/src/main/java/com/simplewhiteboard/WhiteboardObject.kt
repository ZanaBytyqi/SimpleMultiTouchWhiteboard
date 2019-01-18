package com.simplewhiteboard

import android.graphics.Canvas
import android.graphics.Paint

abstract class WhiteboardObject {

    lateinit var paint: Paint

    abstract fun draw(canvas: Canvas)
}

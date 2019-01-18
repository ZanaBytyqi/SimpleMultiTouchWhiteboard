package com.simplewhiteboard

import android.graphics.Canvas
import android.graphics.Path

class SimplePath : WhiteboardObject() {

    private val simplePath: Path = Path()

    override fun draw(canvas: Canvas) {
        canvas.drawPath(simplePath, paint)
    }

    /**
     * Specify the start position.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    fun moveTo(x: Float, y: Float) {
        simplePath.moveTo(x, y)
    }

    /**
     * Specify the end position.
     *
     * @param x The x-coordinate of the end of the line.
     * @param y The y-coordinate of the end of the line.
     */
    fun lineTo(x: Float, y: Float) {
        simplePath.lineTo(x, y)
    }

    /**
     * Draws a quadratic bezier line
     *
     * @param x1 The x-coordinate of the start point on a quadratic curve
     * @param y1 The y-coordinate of the start point on a quadratic curve
     * @param x2 The x-coordinate of the end point on a quadratic curve
     * @param y2 The y-coordinate of the end point on a quadratic curve
     */
    fun quadTo(x1: Float, y1: Float, x2: Float, y2: Float) {
        simplePath.quadTo(x1, y1, x2, y2)
    }

}

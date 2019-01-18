package com.simplewhiteboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View

class WhiteboardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val workingList = SparseArray<WhiteboardObject>()
    private val finalList = ArrayList<WhiteboardObject>()
    private val startingPoints = SparseArray<PointF>()

    init {
        setWillNotDraw(false)
    }

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //go through all paths in final list and draw them into canvas
        finalList.forEach { it.draw(canvas) }

        //go through all paths in working list and draw them into canvas
        for (i in 0 until workingList.size()) {
            workingList.valueAt(i).draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val index = event.actionIndex
        //get id of each touch point
        var id = event.getPointerId(index)

        //get coordinates of each touch point
        val eventX = event.getX(index)
        val eventY = event.getY(index)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                //create a path to be drawn in canvas
                val simplePath = SimplePath()
                simplePath.paint = paint
                //draw initial coordinates
                simplePath.moveTo(eventX, eventY)
                //save the path in the workinglist for each touch point, later to be drawn in onDraw() method
                workingList.put(id, simplePath)

                //save the initial coordinates for each point, that are used for defining the quadratic line
                val pointF = PointF(eventX, eventY)
                startingPoints.put(id, pointF)
            }
            MotionEvent.ACTION_MOVE -> {
                //go through all the touch pointers, and continue drawing quadratic line for each of them
                for (i in 0 until event.pointerCount) {
                    id = event.getPointerId(i)
                    val simplePath = workingList.get(id) as SimplePath
                    simplePath.quadTo(
                            x1 = startingPoints.get(id).x,
                            y1 = startingPoints.get(id).y,
                            x2 = (event.getX(i) + startingPoints.get(id).x) / 2,
                            y2 = (event.getY(i) + startingPoints.get(id).y) / 2
                    )

                    val drawingPoint = PointF(event.getX(i), event.getY(i))
                    startingPoints.put(id, drawingPoint)
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                val simplePath = workingList.get(id) as SimplePath
                //draw last coordinates
                simplePath.lineTo(eventX, eventY)
                //save the final path
                finalList.add(simplePath)
                //remove path from working list because is saved in final list now
                workingList.remove(id)
                startingPoints.remove(id)
            }

        }
        invalidate()
        return true
    }

    fun clearWhiteboard() {
        finalList.clear()
        invalidate()
    }
}

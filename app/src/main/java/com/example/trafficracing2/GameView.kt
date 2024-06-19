package com.example.trafficracing2

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.properties.Delegates

class GameView(var c: Context, var gameTask: GameTask) : View(c) {
    private var mypaint: Paint? = null
    var speed = 1
    private var time = 0
    var score = 0
    private var highScore: Int
    private var myCarPosition = 0
    val otherCars = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0

    private val sharedPreferences: SharedPreferences =
        c.getSharedPreferences("HighScore", Context.MODE_PRIVATE)

    init {
        mypaint = Paint()
        highScore = sharedPreferences.getInt("HighScore", 0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time = time + 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10
        mypaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.redbike, null)

        d.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - carHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
            viewHeight - 2
        )
        d.draw(canvas!!)

        mypaint!!.color = Color.GREEN

        for (i in otherCars.indices) {
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var carY = time - otherCars[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.yellowcar, null)

                d2.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                d2.draw(canvas)

                if (otherCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gameTask.closeGame(score, highScore)
                    }
                }

                if (carY > viewHeight + carHeight) {
                    otherCars.removeAt(i)
                    score++
                    updateHighScore()
                    speed = 1 + Math.abs(score / 8)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mypaint!!.color = Color.WHITE
        mypaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, mypaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, mypaint!!)
        canvas.drawText("High Score : $highScore", 80f, 140f, mypaint!!)

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }

    fun updateHighScore() {
        if (score > highScore) {
            highScore = score
        }
    }
}

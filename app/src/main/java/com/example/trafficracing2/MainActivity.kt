package com.example.trafficracing2


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity(), GameTask {

    lateinit var rootLayout: ConstraintLayout
    lateinit var startBtn: ImageButton
    lateinit var highScoreBtn: ImageButton
    lateinit var mGameView: GameView
    lateinit var score: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn = findViewById(R.id.startBtn)
        highScoreBtn = findViewById(R.id.highScoreBtn)
        rootLayout = findViewById(R.id.constraint)
        score = findViewById(R.id.score)
        mGameView = GameView(this, this)
        sharedPreferences = getSharedPreferences("HighScore", MODE_PRIVATE)

        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            highScoreBtn.visibility = View.GONE
            score.visibility = View.GONE


            // Resetting score to 0
            mGameView.score = 0

            // Resetting speed to 1
            mGameView.speed = 1

            // Clearing otherCars list to remove all yellow vehicles
            mGameView.otherCars.clear()

            // Invalidate the view to force a redraw
            mGameView.invalidate()
        }

        highScoreBtn.setOnClickListener {
            val intent = Intent(this, HighScore::class.java)
            startActivity(intent)
        }


    }

    override fun closeGame(mScore: Int, highScore: Int) {

        updateHighScore(highScore)


        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        highScoreBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE


    }

    override fun updateHighScore(newHighScore: Int) {
        sharedPreferences.edit().putInt("HighScore", newHighScore).apply()
    }

}


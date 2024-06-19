package com.example.trafficracing2

interface GameTask {
    fun closeGame(mScore: Int, highScore: Int)
    fun updateHighScore(newHighScore: Int)
}
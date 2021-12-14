package com.example.headup_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.headup_game.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    lateinit var binding : ActivityGameBinding

    //timer var:
    private lateinit var countDownTimer : CountDownTimer
    private var timeLeftInMilliSec :Long = 60000 // = 1 min
    private var timerRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCelebrity()
        startTimer()

    }

    private fun startTimer() {

        countDownTimer = object: CountDownTimer(timeLeftInMilliSec, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMilliSec = millisUntilFinished
                val seconds : Int = (timeLeftInMilliSec % 60000 / 1000).toInt()
                binding.timerTv.text = "00:$seconds"
            }

            override fun onFinish() {
                Toast.makeText(this@GameActivity,"end timer",Toast.LENGTH_SHORT).show()
            }

        }
        countDownTimer.start()
        timerRunning = true
    }
    fun stopTimer(){
        countDownTimer.cancel()
        timerRunning = false
    }

    private fun getCelebrity() {

    }
}
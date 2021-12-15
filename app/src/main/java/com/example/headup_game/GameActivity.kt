package com.example.headup_game

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Surface
import android.view.View
import com.example.headup_game.databinding.ActivityGameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding : ActivityGameBinding
    var celebrityList : ArrayList<Celebrity.CelebrityItem> = ArrayList()
    var celebrityCounter = 1
    var isLandScape = false
    var gameRunning = true


    //timer var:
    public lateinit var countDownTimer : CountDownTimer
    private var timeLeftInMilliSec :Long = 60000  //
    private var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCelebrity()

        timeLeftInMilliSec = getTimeLeft()
        startTimer()
        setPortraitUI()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val rotation = windowManager.defaultDisplay.rotation
        if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
            Log.d("main","plese rotate device")
            isLandScape = false
            timeLeftInMilliSec = getTimeLeft()
            startTimer()
            setPortraitUI()
        }
        else if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            isLandScape = true
            Log.d("main","plese rotate device")
            timeLeftInMilliSec = getTimeLeft()
            startTimer()
            setLandScapeUI()
            ubdateCelebrity()
        }

        binding.homeBtn.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        binding.restartBtn.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }

    }

    private fun setPortraitUI() {
        binding.apply {
            frameLayout.visibility = View.VISIBLE
            landSframeLayout.visibility = View.INVISIBLE
            nameTv.visibility = View.INVISIBLE
            buttonsLayout.visibility = View.VISIBLE

            if(gameRunning)
            bodyTv.text = "Please Rotate Device"
            else {
                binding.timerTv.text = "00:0"
                binding.landSTimerTv.text = "00:0"
                binding.nameTv?.text = ""
                bodyTv.text = "Game Over!"
            }
        }
    }

    private fun setLandScapeUI() {
        binding.apply {
            frameLayout.visibility = View.INVISIBLE
            buttonsLayout.visibility = View.INVISIBLE

            landSframeLayout.visibility = View.VISIBLE
            nameTv.visibility = View.VISIBLE
        }
    }

    private fun ubdateCelebrity() {
        if (celebrityList.size > 0 && celebrityCounter < celebrityList.size) {
            binding.nameTv?.text = celebrityList[celebrityCounter].name
            binding.bodyTv?.text =
                celebrityList[celebrityCounter].taboo1 + "\n" +
                        celebrityList[celebrityCounter].taboo2 + "\n" +
                        celebrityList[celebrityCounter].taboo3

            celebrityCounter++
        } else {
            binding.timerTv.text = "00:0"
            binding.landSTimerTv.text = "00:0"
            binding.nameTv?.text = ""
            binding.bodyTv?.text = "Game Over"
        }
    }


    private fun startTimer() {
        if(!gameRunning)
            return

        countDownTimer = object: CountDownTimer(timeLeftInMilliSec, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMilliSec = millisUntilFinished
                val seconds : Int = (timeLeftInMilliSec % 60000 / 1000).toInt()

                // check if device in landscape mode
                if(isLandScape)
                    binding.landSTimerTv.text = "00:$seconds"
                else
                    binding.timerTv.text = "00:$seconds"

                timerRunning = true
                saveTimeLeft(timeLeftInMilliSec)
            }

            override fun onFinish() {
                binding.bodyTv.text = "Time Over"
                timerRunning = false
                gameRunning = false
                timeLeftInMilliSec = 60000

                saveTimeLeft(timeLeftInMilliSec)
            }

        }
        countDownTimer.start()
    }


    fun getTimeLeft(): Long{

        val milliSecond : Long
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        milliSecond = sharedPreferences.getString("score", "60000").toString().toLong()  // --> retrieves data from Shared Preferences
        if(timerRunning)
            return milliSecond
        else
            return 60000
    }

    fun saveTimeLeft(milliSecond: Long){
         // We can save data with the following code
            with(sharedPreferences.edit()) {
                putString("score", milliSecond.toString())
                apply()
            }

    }

    private fun getCelebrity() {
        //binding.idLoadingPB.isVisible = true

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dojo-recipes.herokuapp.com/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getCelebrities()
        retrofitData.enqueue(object : Callback<ArrayList<Celebrity.CelebrityItem>> {

            override fun onResponse(call: Call<ArrayList<Celebrity.CelebrityItem>>, response: Response<ArrayList<Celebrity.CelebrityItem>>) {

                for(celebrity in response.body()!!){
                    celebrityList.add(
                        Celebrity.CelebrityItem (
                            celebrity.name,
                            celebrity.taboo1,
                            celebrity.taboo2,
                            celebrity.taboo3,
                            celebrity.pk
                        ))

                }

            }

            override fun onFailure(call: Call<ArrayList<Celebrity.CelebrityItem>>, t: Throwable) {
                Log.d("errror", "${t}")
            }
//
        })
    }
}
package com.example.headup_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.headup_game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
        }

        binding.viewBtn.setOnClickListener {
            intent = Intent(applicationContext, ViewActivity::class.java)
            startActivity(intent)
        }



    }
}
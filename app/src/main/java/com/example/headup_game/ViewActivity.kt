package com.example.headup_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headup_game.databinding.ActivityMainBinding
import com.example.headup_game.databinding.ActivityViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewBinding
    val myAdapter : RecyclerViewAdapter by lazy { RecyclerViewAdapter() }
    var celebrityList : ArrayList<Celebrity.CelebrityItem> = ArrayList()
    lateinit var currentCelebrity: Celebrity.CelebrityItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Adapter setting
        binding.recyclerV.adapter = myAdapter
        binding.recyclerV.layoutManager = LinearLayoutManager(this)

        getAPIdata()

        binding.addBtn.setOnClickListener {
            intent = Intent(applicationContext, AddActivity::class.java)
            startActivity(intent)
        }
        binding.homeBtn.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        binding.searchBtn.setOnClickListener {
            val name = binding.nameET.text.toString()
            searchForClebrity(name)
        }

    }

    private fun searchForClebrity(name: String) {
        var isExist = false
        for(i in 0 until celebrityList.size){
            if(name == celebrityList[i].name) {
                currentCelebrity = celebrityList[i]
                isExist = true
            }
        }
        if(isExist){
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("name", currentCelebrity.name)
            intent.putExtra("word1", currentCelebrity.taboo1)
            intent.putExtra("word2", currentCelebrity.taboo2)
            intent.putExtra("word3", currentCelebrity.taboo3)
            intent.putExtra("id", currentCelebrity.pk)
            startActivity(intent)


        }
    }

    private fun getAPIdata() {
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
                //binding.idLoadingPB.isVisible = false
                myAdapter.setCelebrityList(celebrityList)

            }

            override fun onFailure(call: Call<ArrayList<Celebrity.CelebrityItem>>, t: Throwable) {
                Log.d("errror", "${t}")
            }
//
        })
    }
}



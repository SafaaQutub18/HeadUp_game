package com.example.headup_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.headup_game.databinding.ActivityAddBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            addBtn.setOnClickListener {
                val name =  nameET.text.toString()
                val taboo1 = taboo1ET.text.toString()
                val taboo2 = taboo2ET.text.toString()
                val taboo3 = taboo3ET.text.toString()
                if(name!="" || taboo1!=""||taboo2!=""||taboo3!="")
                    addCelebrity(name,taboo1,taboo2,taboo3)
                else
                    Toast.makeText(this@AddActivity , "Please complete the information.", Toast.LENGTH_SHORT).show()
            }
            backBtn.setOnClickListener{
                intent = Intent(applicationContext, ViewActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun addCelebrity(name: String, taboo1: String, taboo2: String, taboo3: String) {
        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE;

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dojo-recipes.herokuapp.com")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.postCelebrity(Celebrity.CelebrityItem(name,taboo1,taboo2,taboo3))
        retrofitData.enqueue(object : Callback<Celebrity.CelebrityItem> {
            override fun onResponse(call: Call<Celebrity.CelebrityItem>, response: Response<Celebrity.CelebrityItem>) {
                Toast.makeText(this@AddActivity, "User added to API", Toast.LENGTH_SHORT).show();
                // below line is for hiding our progress bar.
                binding.loadingPB.visibility = View.GONE;

                binding.nameET.setText("")
                binding.taboo1ET.setText("");
                binding.taboo2ET.setText("");
                binding.taboo3ET.setText("");

                // we are getting response from our body
                // and passing it to our modal class.
                val responseFromAPI: Celebrity.CelebrityItem? = response.body()
                val responseString = """
                    Response Code : ${response.code()}
                    Name : ${responseFromAPI?.name }
                    """.trimIndent()

                Log.d("Main" , responseString)


            }
            override fun onFailure(call: Call<Celebrity.CelebrityItem>, t: Throwable) {
                Toast.makeText(this@AddActivity, "Error: user not added to API", Toast.LENGTH_LONG)
            }
        })
    }

}
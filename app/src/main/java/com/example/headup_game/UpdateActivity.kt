package com.example.headup_game


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.headup_game.databinding.ActivityUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieve this data
        val currentName  = intent.extras?.getString("name")!!
        val currentword1 = intent.extras?.getString("word1")!!
        val currentword2 = intent.extras?.getString("word2")!!
        val currentword3 = intent.extras?.getString("word3")!!
        val id = intent.extras?.getInt("id")!!

        binding.apply {

            nameET.setText(currentName)
            taboo1ET.setText(currentword1)
            taboo2ET.setText(currentword2)
            taboo3ET.setText(currentword3)

            updateBtn.setOnClickListener {
                val name =  nameET.text.toString()
                val taboo1 = taboo1ET.text.toString()
                val taboo2 = taboo2ET.text.toString()
                val taboo3 = taboo3ET.text.toString()
                if(name!="" || taboo1!=""||taboo2!=""||taboo3!="")
                    updateCelebrity(name,taboo1,taboo2,taboo3 ,id)
                else
                    Toast.makeText(this@UpdateActivity , "Please complete the information.", Toast.LENGTH_SHORT).show()

            }

            deleteBtn.setOnClickListener{
                deleteCelebrity(id)
            }

            backBtn.setOnClickListener{
                intent = Intent(applicationContext, ViewActivity::class.java)
                startActivity(intent)
            }

        }
    }
    private fun updateCelebrity(name: String, taboo1: String, taboo2: String, taboo3: String, id: Int) {

// below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE;

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dojo-recipes.herokuapp.com")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.updateCelebrity(id, Celebrity.CelebrityItem(name,taboo1,taboo2,taboo3,id))
        retrofitData.enqueue(object : Callback<Celebrity.CelebrityItem> {
            override fun onResponse(call: Call<Celebrity.CelebrityItem>, response: Response<Celebrity.CelebrityItem>) {
                Toast.makeText(this@UpdateActivity, "User info. has been modified", Toast.LENGTH_SHORT).show();
                // below line is for hiding our progress bar.
                binding.loadingPB.visibility = View.GONE;

                binding.nameET.setText("")
                binding.taboo1ET.setText("");
                binding.taboo2ET.setText("");
                binding.taboo3ET.setText("");


            }
            override fun onFailure(call: Call<Celebrity.CelebrityItem>, t: Throwable) {
                Toast.makeText(this@UpdateActivity, "Error: celebrity not ubdated", Toast.LENGTH_LONG)
            }
        })
    }



    private fun deleteCelebrity(id: Int) {
        binding.loadingPB.visibility = View.VISIBLE;

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dojo-recipes.herokuapp.com")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.deleteCelebrity(id)
        retrofitData.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@UpdateActivity, "User info. has been deleted", Toast.LENGTH_SHORT).show();
                // below line is for hiding our progress bar.
                binding.loadingPB.visibility = View.GONE;

                binding.nameET.setText("")
                binding.taboo1ET.setText("");
                binding.taboo2ET.setText("");
                binding.taboo3ET.setText("");

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UpdateActivity, "Error: user not added to API", Toast.LENGTH_LONG)
            }
        })
    }


}
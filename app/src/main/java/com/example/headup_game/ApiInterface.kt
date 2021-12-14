package com.example.headup_game


import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @GET("/celebrities/")
    fun getCelebrities(): Call<ArrayList<Celebrity.CelebrityItem>>

    @POST("/celebrities/")
    fun postCelebrity(@Body userData: Celebrity.CelebrityItem): Call<Celebrity.CelebrityItem>

    @PUT("/celebrities/{id}")
    fun updateCelebrity(@Path("id") id:Int, @Body userData: Celebrity.CelebrityItem): Call<Celebrity.CelebrityItem>

    @DELETE("/celebrities/{id}")
    fun deleteCelebrity(@Path("id") id:Int):Call<Void>


}
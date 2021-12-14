package com.example.headup_game

class Celebrity {

    var data: ArrayList<CelebrityItem>? = null

    data class CelebrityItem(
        val name: String,
        val taboo1: String,
        val taboo2: String,
        val taboo3: String,
        val pk: Int? = null
    )

}
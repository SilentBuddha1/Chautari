package com.example.quotes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val Base_URl = "https://zenquotes.io/api/"

    private fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(Base_URl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val quoteApi : QuoteApi = getInstance().create(QuoteApi::class.java)
}
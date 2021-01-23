package com.example.seek.data.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL = "http://www.boredapi.com"

    val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val boredApiInterface: BoredApiInterface by lazy {
        retrofitClient
            .build()
            .create(BoredApiInterface::class.java)
    }

}

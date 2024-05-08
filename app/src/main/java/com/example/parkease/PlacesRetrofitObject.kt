package com.example.parkease

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlacesRetrofitObject {
    private const val BASE_URL = "https://maps.googleapis.com/"
    val retrofitService: PlacesApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(PlacesApiService::class.java)
    }
}
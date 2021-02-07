package com.seif.catfactsapp

import com.seif.catfactsapp.Api.CatJson
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {
    // create @Get request
    @GET("/facts/random")
    fun getCatFacts(): Call<CatJson>

}
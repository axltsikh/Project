package com.example.project.rertofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GsonService {
    @GET("https://v6.exchangerate-api.com/v6/7d4ff7965e65c18b763ca110/pair/{convertFrom}/{convertTo}")
    fun getExchange(
        @Path("convertFrom") convertFrom:String,
        @Path("convertTo") convertTo:String
    ): Call<Convert>
}
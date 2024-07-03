package com.example.apiexamnewsapp.data

import com.example.apiexamnewsapp.data.model.Products
import retrofit2.http.GET


interface Api {

//    @GET("products/{type}")
//    suspend fun getProductsList(
//        @Query("type") type: String,
//        @Query("page") page: Int,
//        @Query("api_key") apiKey: String
//
//    ): Products


    @GET("products")
    suspend fun getProductsList(): Products

    companion object {
        const val BASE_URL = "https://dummyjson.com/"
    }
}
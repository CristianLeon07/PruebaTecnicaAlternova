package com.example.pruebatecnicaalternova.data.external

import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET
    suspend fun getApi(
        @Url url:String,
        @Query("id") id:Int
    ): Response<ProductsAll>?


    @GET
    suspend fun getApiArray(
        @Url url:String,
    ): Response<ModelResponseApiArray>?

    @POST
    suspend fun postApiArray(
        @Url url:String,
    ): Response<ModelResponseApiArray>?

}
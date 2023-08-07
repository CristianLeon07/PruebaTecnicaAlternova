package com.example.pruebatecnicaalternova.data.external

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class ReturnData(val data: Object?, val success: Boolean)

class APIConecction {

    public fun getData(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endpoint().url_server)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
package com.example.pruebatecnicaalternova.data.external

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class ProductsAll(
    @SerializedName("id")   var id: Int? = null,
    @SerializedName("name")    val name: String? = "",
    @SerializedName("unit_price")   val unit_price: Int? = 0,
    @SerializedName("stock")    val stock: Int? = null,
    @SerializedName("image") val image: String? = "",
    @SerializedName("description")val description: String =""

)

data class ModelResponseApiArray(
    @SerializedName("products") var data: JsonArray?= null
)
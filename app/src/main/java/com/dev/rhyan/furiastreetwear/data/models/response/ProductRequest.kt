package com.dev.rhyan.furiastreetwear.data.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ProductsRequest(
    val name : String,
    val image : String,
    val link : String
)

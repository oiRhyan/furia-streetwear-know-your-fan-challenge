package com.dev.rhyan.furiastreetwear.data.repositories

import com.dev.rhyan.furiastreetwear.data.models.response.ProductsRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class ProductRepository(val service : HttpClient) {

    suspend fun getAllProducts() : List<ProductsRequest> {
        val request : HttpResponse = service.get("https://furia-try-on.onrender.com/products")
        if(request.status == HttpStatusCode.OK) {
            return request.body<List<ProductsRequest>>()
        } else {
            throw Exception("Product response error: ${request.status}")
        }
    }

}
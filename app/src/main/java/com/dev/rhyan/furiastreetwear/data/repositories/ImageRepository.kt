package com.dev.rhyan.furiastreetwear.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.dev.rhyan.furiastreetwear.data.models.request.ImageProcessBody
import com.dev.rhyan.furiastreetwear.data.models.response.IAResponse
import com.dev.rhyan.furiastreetwear.data.utils.CloundinaryService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class ImageRepository(val service : HttpClient) {

    suspend fun renderImage(body: ImageProcessBody): Bitmap {
        val response: HttpResponse = service.post("https://furia-try-on.onrender.com/try-on") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        if (response.status == HttpStatusCode.OK) {
            val responseObj = response.body<IAResponse>()
            val imageBytes = Base64.decode(responseObj.image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            return bitmap
        } else {
            throw Exception("Erro ao processar imagem: ${response.status}")
        }
    }

    suspend fun uploadImage(image : Uri, context: Context) : String {
        val response = CloundinaryService.uploadImageProfile(image = image, context = context)
        if(response != null) {
            return response
        } else {
            throw Exception("Erro ao realizar upload de imagem: ${response}")
        }
    }
}
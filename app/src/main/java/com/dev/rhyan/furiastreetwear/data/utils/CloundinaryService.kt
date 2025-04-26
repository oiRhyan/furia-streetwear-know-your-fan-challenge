package com.dev.rhyan.furiastreetwear.data.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

interface CloundinaryService {

    companion object {

        suspend fun uploadImageProfile(
            image : Uri,
            context: Context,
            dispatcher: CoroutineDispatcher = Dispatchers.IO
        ): String? = withContext(dispatcher) {
            val imageUri = image

            if (imageUri == null) {
                Log.e("Upload", "Imagem não selecionada")
                return@withContext null
            }

            val contentResolver = context.contentResolver
            val inputStream = try {
                contentResolver.openInputStream(imageUri)
            } catch (e: Exception) {
                Log.e("Upload", "Erro ao abrir imagem", e)
                return@withContext null
            }

            val imageBytes = inputStream?.use { it.readBytes() } ?: return@withContext null

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file", "profile.jpg",
                    imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                )
                .addFormDataPart("upload_preset", "grcusjqq")
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/dvrwomdhj/image/upload")
                .post(requestBody)
                .build()

            val client = OkHttpClient()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    Log.e("Upload", "Erro: $responseBody")
                    return@withContext null
                }

                val json = JSONObject(responseBody)
                return@withContext json.optString("secure_url")
            } catch (e: Exception) {
                Log.e("Upload", "Erro na requisição", e)
                return@withContext null
            }
        }

    }
}
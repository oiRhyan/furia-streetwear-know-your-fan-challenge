package com.dev.rhyan.furiastreetwear.ui.viewmodels

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.rhyan.furiastreetwear.data.models.request.ImageProcessBody
import com.dev.rhyan.furiastreetwear.data.repositories.ImageRepository
import com.dev.rhyan.furiastreetwear.ui.state.ImageIAState
import com.dev.rhyan.furiastreetwear.ui.state.ImageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import java.io.File
import java.io.FileOutputStream

class ImageProcessViewModel(
    val repository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ImageUiState>(ImageUiState())
    val uiState = _uiState.asStateFlow()

    private val _iaState = MutableStateFlow<ImageIAState>(ImageIAState.Empty)
    val iaState = _iaState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(
                onPersonImageChanged = { image ->
                    _uiState.value = _uiState.value.copy(personImage = image)
                }
            )
        }
    }

    fun renderImage(clothImage : String, context: Context) {
        viewModelScope.launch {
            _iaState.value = ImageIAState.Loading
            try {
                if(_uiState.value.personImage != null) {
                    val imageUrl = repository.uploadImage(image = _uiState.value.personImage!!, context)
                    val response = repository.renderImage(
                        ImageProcessBody(
                            cloth_image = clothImage,
                            pose_image = imageUrl
                        )
                    )
                    _iaState.value = ImageIAState.Sucess(response)
                }
            } catch (e : Exception) {
                Log.e("Connection Exception", "${e.message}")
            }
        }
    }

    fun saveImageToStorage(bitmap: ImageBitmap, context: Context): File? {
        val contentResolver = context.contentResolver
        val imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FuriaStreetwear")
            }
        }

        try {
            val imageUri = contentResolver.insert(imageCollection, imageDetails)
            imageUri?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val bitmapImage = bitmap.asAndroidBitmap()
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    return File(uri.path!!)
                }
            }
        } catch (e: IOException) {
            Log.e("SaveImage", "Error saving image to storage", e)
        }
        return null
    }


    fun shareImage(context: Context, imageFile: File) {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
        } else {
            Uri.fromFile(imageFile)
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Compartilhar imagem")
        context.startActivity(chooser)
    }




}
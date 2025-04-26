package com.dev.rhyan.furiastreetwear.ui.state

import android.graphics.Bitmap
import android.net.Uri

data class ImageUiState(
    val personImage : Uri? = null,
    val onPersonImageChanged : (Uri) -> Unit = {},
)

sealed class ImageIAState() {
    data object Empty : ImageIAState()
    data object Loading : ImageIAState()
    data class Sucess(val image : Bitmap) : ImageIAState()
    data class Error(val error : String) : ImageIAState()
}
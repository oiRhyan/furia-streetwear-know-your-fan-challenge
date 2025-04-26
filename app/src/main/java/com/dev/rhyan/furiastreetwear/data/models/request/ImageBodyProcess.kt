package com.dev.rhyan.furiastreetwear.data.models.request

import kotlinx.serialization.Serializable

@Serializable
data class ImageProcessBody(
    val pose_image : String,
    val cloth_image : String
)

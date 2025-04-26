package com.dev.rhyan.furiastreetwear.ui.state

import kotlinx.serialization.Serializable

data class FormDataUiState(
    val nome: String = "",
    val email: String = "",
    var redeFavorita: String = "",
    val estiloFavorito: String = "",
    val comprouProduto: String = "",
    val compartilhaNoticias: Boolean = false,
    val onNameChanged : (String) -> Unit = {},
    val onEmailChanged : (String) -> Unit = {},
    val onRedeFavoritaChanged : (String) -> Unit = {},
    val onEstiloFavoritoChanged : (String) -> Unit = {},
    val onProdutoChanged : (String) -> Unit = {},
    val onSharedNoticesChanged : (Boolean) -> Unit = {},
)

fun FormDataUiState.toFormData() = FormData(
    nome = nome,
    email = email,
    redeFavorita = redeFavorita,
    estiloFavorito = estiloFavorito,
    comprouProduto = comprouProduto,
    compartilhaNoticias = compartilhaNoticias
)
@Serializable
data class FormData(
    val nome: String = "",
    val email: String = "",
    val redeFavorita: String = "",
    val estiloFavorito: String = "",
    val comprouProduto: String = "",
    val compartilhaNoticias: Boolean = false
)

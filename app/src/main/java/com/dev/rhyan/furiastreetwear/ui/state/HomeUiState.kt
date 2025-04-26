package com.dev.rhyan.furiastreetwear.ui.state

import com.dev.rhyan.furiastreetwear.data.models.response.ProductsRequest

sealed class HomeUiState() {
    data object Loading : HomeUiState()
    data class Sucess(val products : List<ProductsRequest>) : HomeUiState()
    data class Error(val error : String) : HomeUiState()
}
package com.dev.rhyan.furiastreetwear.ui.viewmodels


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.rhyan.furiastreetwear.data.repositories.ProductRepository
import com.dev.rhyan.furiastreetwear.ui.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class ProductViewModel(
    val repository : ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _selectedProduct = MutableStateFlow<String?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                _uiState.value = HomeUiState.Sucess(products = response)
            } catch (e : Exception) {
                Log.e("Connection Exception", "${e.message}")
            }
        }
    }

    fun goToProduct(context : Context, url : String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }

    fun selectProduct(productImage : String) {
        _selectedProduct.value = productImage
    }

}
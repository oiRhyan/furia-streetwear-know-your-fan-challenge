package com.dev.rhyan.furiastreetwear.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.dev.rhyan.furiastreetwear.ui.state.FormData
import com.dev.rhyan.furiastreetwear.ui.state.FormDataUiState
import com.dev.rhyan.furiastreetwear.ui.state.toFormData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormDataViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow<FormDataUiState>(FormDataUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(
                onNameChanged = { name ->
                    _uiState.value = _uiState.value.copy(nome = name)
                },
                onEmailChanged = { email ->
                    _uiState.value = _uiState.value.copy(email = email)
                },
                onProdutoChanged = { produto ->
                    _uiState.value = _uiState.value.copy(comprouProduto = produto)
                },
                onEstiloFavoritoChanged = { estilo ->
                    _uiState.value = _uiState.value.copy(estiloFavorito = estilo)
                },
                onRedeFavoritaChanged = { rede ->
                    _uiState.value = _uiState.value.copy(redeFavorita = rede)
                },
                onSharedNoticesChanged = { termos ->
                    _uiState.value = _uiState.value.copy(compartilhaNoticias = termos)
                }
            )
        }
    }

    fun salvarFormularioFirestore(onResult: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val form = _uiState.value.toFormData()
        db.collection("formularios")
            .add(form)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
}
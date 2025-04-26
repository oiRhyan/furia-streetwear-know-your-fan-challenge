package com.dev.rhyan.furiastreetwear.ui.state

import com.dev.rhyan.furiastreetwear.data.models.response.UserDataProvider

sealed class LoginUIState {
    data object isEmpty : LoginUIState()
    data class Sucess(val data: UserDataProvider?, val message: String) : LoginUIState()
    data class Error(val message : String) : LoginUIState()
}
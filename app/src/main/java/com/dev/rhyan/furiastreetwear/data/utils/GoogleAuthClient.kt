package com.dev.rhyan.furiastreetwear.data.utils

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.dev.rhyan.furiastreetwear.data.models.response.UserDataProvider
import com.dev.rhyan.furiastreetwear.ui.state.LoginUIState
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.firebase.Firebase
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class GoogleAuthClient(
    private val context: Context,
    private val onTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            onTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        Log.d("GoogleAuthClient", "signIn result: $result")
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("740479879745-n2l1pegd97bpvu4c277tmnd251kd7cl7.apps.googleusercontent.com")
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun signOut() {
        try {
            onTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserDataProvider? = auth.currentUser?.run {
        UserDataProvider(
            userid = uid,
            username = displayName.toString(),
            photoUrl = photoUrl.toString()
        )
    }

    suspend fun signInWithIntent(intent: Intent): LoginUIState {
        val credential = onTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        Log.d("GoogleAuthClient", "Google ID Token: $googleIdToken")
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            val data = user?.run {
                UserDataProvider(
                    username = user.displayName.toString(),
                    userid = uid,
                    photoUrl = user.photoUrl.toString()
                )
            }
            Log.d("GoogleAuthClient", "User signed in: $data")

            LoginUIState.Sucess(
                data = data,
                message = "Success"
            )
        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Error signing in: ${e.message}", e)
            LoginUIState.Error(
                message = "Error during authentication: ${e.message}"
            )
        }
    }
}
package com.dev.rhyan.furiastreetwear

import android.app.Application
import com.dev.rhyan.furiastreetwear.data.di.network
import com.dev.rhyan.furiastreetwear.data.di.repository
import com.dev.rhyan.furiastreetwear.data.di.viewmodel
import com.google.firebase.Firebase
import com.google.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FuriaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        startKoin {
            androidContext(this@FuriaApplication)
            modules(network, repository, viewmodel)
        }
    }
}
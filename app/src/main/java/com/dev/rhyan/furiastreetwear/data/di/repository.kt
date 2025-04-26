package com.dev.rhyan.furiastreetwear.data.di

import com.dev.rhyan.furiastreetwear.data.repositories.ImageRepository
import com.dev.rhyan.furiastreetwear.data.repositories.ProductRepository
import org.koin.dsl.module

val repository = module {
    single { ProductRepository(get()) }
    single { ImageRepository(get()) }
}
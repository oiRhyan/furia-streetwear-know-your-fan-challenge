package com.dev.rhyan.furiastreetwear.data.di

import com.dev.rhyan.furiastreetwear.ui.viewmodels.AuthenticateViewModel
import com.dev.rhyan.furiastreetwear.ui.viewmodels.FormDataViewModel
import com.dev.rhyan.furiastreetwear.ui.viewmodels.ImageProcessViewModel
import com.dev.rhyan.furiastreetwear.ui.viewmodels.ProductViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewmodel = module {
    viewModel {
        ProductViewModel(get())
    }
    viewModel {
        ImageProcessViewModel(get())
    }

    viewModel {
        AuthenticateViewModel()
    }

    viewModel {
        FormDataViewModel()
    }
}
package com.brustoloni.revoluttestapp.di

import android.arch.lifecycle.ViewModelProvider
import com.brustoloni.revoluttestapp.di.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}
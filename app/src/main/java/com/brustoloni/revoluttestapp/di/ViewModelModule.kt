package com.brustoloni.revoluttestapp.di

import android.arch.lifecycle.ViewModel
import com.brustoloni.revoluttestapp.presentation.conversions.ConversionsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ConversionsViewModel::class)
    abstract fun bindConversionsViewModel(viewModel: ConversionsViewModel) : ViewModel
}
package com.brustoloni.revoluttestapp.presentation.conversions.di

import android.app.Activity
import com.brustoloni.revoluttestapp.presentation.conversions.ConversionsActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap


@Module(subcomponents = arrayOf(ConversionsActivitySubcomponent::class))
abstract class ConversionsActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(ConversionsActivity::class)
    abstract fun bindConversionsActivityInjectorFactory(builder: ConversionsActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}
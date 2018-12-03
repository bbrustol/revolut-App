package com.brustoloni.revoluttestapp.presentation.conversions.di

import com.brustoloni.revoluttestapp.presentation.conversions.ConversionsActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector


@Subcomponent(modules = arrayOf(ConversionsFragmentModule::class))
interface ConversionsActivitySubcomponent : AndroidInjector<ConversionsActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ConversionsActivity>()
}
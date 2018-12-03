package com.brustoloni.revoluttestapp.presentation.conversions.di

import android.support.v4.app.Fragment
import com.brustoloni.revoluttestapp.presentation.conversions.ConversionsFragment
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Subcomponent/*(modules = ...)*/
interface ConversionsFragmentSubcomponent: AndroidInjector<ConversionsFragment> {
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<ConversionsFragment>()
}

@Module(subcomponents = arrayOf(ConversionsFragmentSubcomponent::class))
abstract class ConversionsFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(ConversionsFragment::class)
    abstract fun bindConversionsFragmentInjectorFactory(builder: ConversionsFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}
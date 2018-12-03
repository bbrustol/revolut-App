package com.brustoloni.revoluttestapp.di

import com.brustoloni.revoluttestapp.presentation.conversions.di.ConversionsActivityModule
import com.brustoloni.revoluttestapp.ApplicationConfiguration
import com.brustoloni.revoluttestapp.business.ConversionsBusiness
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        ViewModelFactoryModule::class,
        RepositoryModule::class,
        ConversionsBusiness::class,
        UseCasesModule::class,
        ViewModelModule::class,
        ConversionsActivityModule::class
        ))

interface ApplicationComponent {
    fun inject(app: ApplicationConfiguration)
}
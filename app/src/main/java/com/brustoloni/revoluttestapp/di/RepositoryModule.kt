package com.brustoloni.revoluttestapp.di

import com.brustoloni.revoluttestapp.data.repository.ConversionsRepository
import com.brustoloni.revoluttestapp.data.source.ConversionsApi
import com.brustoloni.revoluttestapp.data.source.ConversionsApiImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun providesConversionsRepository(conversionsApi: ConversionsApi): ConversionsRepository = ConversionsApiImp(conversionsApi)
}
package com.brustoloni.revoluttestapp.di

import com.brustoloni.revoluttestapp.business.interectors.ConversionsInteractor
import com.brustoloni.revoluttestapp.data.model.ConversionsUseCases
import com.brustoloni.revoluttestapp.data.repository.ConversionsRepository
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun providesConversionsUseCases(conversionsRepository: ConversionsRepository): ConversionsUseCases = ConversionsInteractor(conversionsRepository)
}
package com.brustoloni.revoluttestapp.business

import com.brustoloni.revoluttestapp.data.source.ConversionsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ConversionsBusiness: BaseBusiness() {
    @Provides
    fun providesCoinMarketCapApi(retrofit: Retrofit) = retrofit.create(ConversionsApi::class.java)
}

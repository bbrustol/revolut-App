package com.brustoloni.revoluttestapp.data.source

import com.brustoloni.revoluttestapp.BuildConfig
import com.brustoloni.revoluttestapp.data.model.ConversionsModel
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Query

interface ConversionsApi {
    @GET(BuildConfig.LATEST)
    fun getRates(@Query("base" )base:String): Single<ConversionsModel>
}
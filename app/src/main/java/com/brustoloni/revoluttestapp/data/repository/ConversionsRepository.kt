package com.brustoloni.revoluttestapp.data.repository

import com.brustoloni.revoluttestapp.data.model.ConversionsModel
import io.reactivex.Single

interface ConversionsRepository {
    fun getRates(base:String): Single<ConversionsModel>
}
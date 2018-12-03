package com.brustoloni.revoluttestapp.data.source

import com.brustoloni.revoluttestapp.data.model.ConversionsModel
import com.brustoloni.revoluttestapp.data.repository.ConversionsRepository
import io.reactivex.Single

class ConversionsApiImp(private val conversionsApi: ConversionsApi) : ConversionsRepository {
    override fun getRates(base: String): Single<ConversionsModel> = conversionsApi.getRates(base)
}
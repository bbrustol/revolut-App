package com.brustoloni.revoluttestapp.business.interectors


import com.brustoloni.revoluttestapp.data.model.ConversionsModel
import com.brustoloni.revoluttestapp.data.model.ConversionsUseCases
import com.brustoloni.revoluttestapp.data.repository.ConversionsRepository
import io.reactivex.Single

class ConversionsInteractor(private val conversionsRepository: ConversionsRepository) : ConversionsUseCases {
    override fun getConversions(base: String): Single<ConversionsModel> {
        return conversionsRepository.getRates(base)
            .map { conversions -> conversions }
    }
}
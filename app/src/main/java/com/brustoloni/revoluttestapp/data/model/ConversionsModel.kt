package com.brustoloni.revoluttestapp.data.model

import android.os.Parcelable
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize

interface ConversionsUseCases {
    fun getConversions(base: String) : Single<ConversionsModel>
}

val emptyConversionsModel = ConversionsModel("","", LinkedHashMap<String, Float>())

@Parcelize
data class ConversionsModel(val base: String,
                            val date: String,
                            val rates: LinkedHashMap<String, Float>) : Parcelable


@Parcelize
data class CurrencysModel(var code: String,
                          var value: Float) : Parcelable
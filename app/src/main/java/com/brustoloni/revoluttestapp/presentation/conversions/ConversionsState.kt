package com.brustoloni.revoluttestapp.presentation.conversions

import com.brustoloni.revoluttestapp.data.model.ConversionsModel

sealed class ConversionsState {
    abstract var conversions: ConversionsModel
}

data class DefaultState(override var conversions: ConversionsModel) : ConversionsState()

data class ErrorState(val errorMessage: String, override var conversions: ConversionsModel) : ConversionsState()
package com.brustoloni.revoluttestapp.infrastruture

import java.util.Locale

class Constants private constructor() {
    companion object {
        const val INSTANTIATING_NOT_ALLOWED = "Instantiating not allowed"
    }

    class AppLocale private constructor() {
        init {
            throw ExceptionInInitializerError(INSTANTIATING_NOT_ALLOWED)
        }

        companion object {
            val BR_APP_LOCALE = Locale("pt", "BR")
            val US_APP_LOCALE = Locale("en", "US")
        }
    }
}
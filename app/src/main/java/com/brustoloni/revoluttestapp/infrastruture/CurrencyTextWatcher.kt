package br.com.itau.cartoes.infrastructure

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.brustoloni.revoluttestapp.infrastruture.Constants
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.math.BigDecimal

import java.text.NumberFormat

class CurrencyTextWatcher(
    private val mEditText: EditText,
    private val mCurrencyTextWatcherListener: CurrencyTextWatcherListener
) : TextWatcher {

    interface CurrencyTextWatcherListener {
        fun onValueChanged(value: Double)
    }

    private val STR_ZERO = "0"
    private val INITIAL_POSITION = -1
    private val INITIAL_POSITION_CURSOR = -1
    private val log = AnkoLogger(this.javaClass)
    private var mLastAmount = ""
    private var mLastCursorPosition = -1
    var mValue: Double = 0.toDouble()


    private fun clearCurrencyToNumber(currencyValue: String?): String {
        val result: String
        if (currencyValue == null) {
            result = "0.00"
        } else {
            result = currencyValue.replace("[(a-z)|(A-Z)|($,. )]".toRegex(), "")
        }

        return result
    }

    private fun transformToCurrency(value: String): String {
        val percent100 = 100
        mValue = java.lang.Double.parseDouble(clearCurrencyToNumber(value)) / percent100

        var formatted = NumberFormat.getCurrencyInstance(Constants.AppLocale.BR_APP_LOCALE).format(mValue)
        formatted = formatted.replace("[^(0-9).,]".toRegex(), "")

        return String.format("%s", formatted)
    }

    fun currencyToDecimal(currencyValue: String): BigDecimal {
        return currencyValue.replace("[(a-z)|(A-Z)|($, )]".toRegex(), "").toBigDecimal()
    }

    override fun onTextChanged(amount: CharSequence, start: Int, before: Int, count: Int) {
        if (mLastCursorPosition == INITIAL_POSITION) {
            transformToCurrency(STR_ZERO)
        }

        if (amount.toString() != mLastAmount) {
            try {
                val formattedAmount = transformToCurrency(amount.toString())
                mEditText.removeTextChangedListener(this)
                mEditText.setText(formattedAmount)
                mEditText.setSelection(formattedAmount.length)
                mEditText.addTextChangedListener(this)

                if (mLastCursorPosition != mLastAmount.length && mLastCursorPosition != INITIAL_POSITION) {
                    val lengthDelta = formattedAmount.length - mLastAmount.length
                    val newCursorOffset = Math.max(
                        INITIAL_POSITION_CURSOR,
                        Math.min(formattedAmount.length, mLastCursorPosition + lengthDelta)
                    )
                    mEditText.setSelection(newCursorOffset)
                }
            } catch (e: Exception) {
                log.error { e.message }
            }
        }
    }

    override fun afterTextChanged(editable: Editable) {
        mCurrencyTextWatcherListener.onValueChanged(mValue)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        val value = s.toString()

        if (mLastCursorPosition == -1) {
            transformToCurrency(STR_ZERO)
        }

        if (!value.isEmpty()) {
            mLastAmount = transformToCurrency(value)
            mLastCursorPosition = mEditText.selectionStart
        }
    }
}
package com.brustoloni.revoluttestapp.infrastruture;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Some note <br/>
 * <li>Always use locale US instead of default to make DecimalFormat work well in all language</li>
 */
public class CurrencyEditText extends android.support.v7.widget.AppCompatEditText {
    private static String prefix = "VND ";
    private static final int MAX_LENGTH = 20;
    private static final int MAX_DECIMAL = 2;
    private CurrencyTextWatcher currencyTextWatcher = new CurrencyTextWatcher(this, prefix);

    public CurrencyEditText(Context context) {
        this(context, null);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.setHint(prefix);
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            this.addTextChangedListener(currencyTextWatcher);
        } else {
            this.removeTextChangedListener(currencyTextWatcher);
        }
        handleCaseCurrencyEmpty(focused);
    }

    /**
     * When currency empty <br/>
     * + When focus EditText, set the default text = prefix (ex: VND) <br/>
     * + When EditText lose focus, set the default text = "", EditText will display hint (ex:VND)
     */
    private void handleCaseCurrencyEmpty(boolean focused) {
        if (focused) {
            if (getText().toString().isEmpty()) {
                setText(prefix);
            }
        } else {
            if (getText().toString().equals(prefix)) {
                setText("");
            }
        }
    }

    private static class CurrencyTextWatcher implements TextWatcher {
        private final EditText mEditText;
        private static final String STR_ZERO = "0";
        private static final int INITIAL_POSITION = -1;
        private static final int INITIAL_POSITION_CURSOR = -1;

        private String mLastAmount = "";
        private int mLastCursorPosition = -1;
        private double mValue;
        String formattedAmount;

        CurrencyTextWatcher(EditText editText, String prefix) {
            this.mEditText = editText;
        }

        private static String clearCurrencyToNumber(String currencyValue) {
            String result;
            if (currencyValue == null) {
                result = "0,00";
            } else {
                result = currencyValue.replaceAll("[(a-z)|(A-Z)|($,. )]", "");
            }

            return result;
        }

        private String transformToCurrency(String value) {
            int percent100 = 100;
            mValue = Double.parseDouble(clearCurrencyToNumber(value)) / percent100;

            Locale BR_APP_LOCALE = new Locale("pt", "BR");
            String formatted = NumberFormat.getCurrencyInstance(BR_APP_LOCALE).format(mValue);
            formatted = formatted.replaceAll("[^(0-9).,]", "");

            return String.format("%s", formatted);
        }

        @Override
        public void onTextChanged(CharSequence amount, int start, int before, int count) {
            if (mLastCursorPosition == INITIAL_POSITION) {
                transformToCurrency(STR_ZERO);
            }

            if (!amount.toString().equals(mLastAmount)) {
                try {
                    formattedAmount = transformToCurrency(amount.toString());
                    mEditText.removeTextChangedListener(this);
                    mEditText.setText(formattedAmount);
                    mEditText.setSelection(formattedAmount.length());
                    mEditText.addTextChangedListener(this);

                    if (mLastCursorPosition != mLastAmount.length() && mLastCursorPosition != INITIAL_POSITION) {
                        int lengthDelta = formattedAmount.length() - mLastAmount.length();
                        int newCursorOffset = Math.max(
                                INITIAL_POSITION_CURSOR,
                                Math.min(formattedAmount.length(), mLastCursorPosition + lengthDelta));
                        mEditText.setSelection(newCursorOffset);
                    }
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String value = s.toString();

            if (mLastCursorPosition == -1) {
                transformToCurrency(STR_ZERO);
            }

            if (!value.isEmpty()) {
                mLastAmount = transformToCurrency(value);
                mLastCursorPosition = mEditText.getSelectionStart();
            }
        }

    }
}
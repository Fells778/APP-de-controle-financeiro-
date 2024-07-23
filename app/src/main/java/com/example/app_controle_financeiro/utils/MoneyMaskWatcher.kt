package com.example.app_controle_financeiro.utils

import android.icu.text.DecimalFormat
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.math.BigDecimal
import java.util.Locale

class MoneyMaskWatcher (private val editText: EditText) : TextWatcher {
    private var isUpdating: Boolean = false
    private val locale = Locale("pt", "BR")
    private val decimalFormat = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat

    init {
        decimalFormat.maximumFractionDigits = 2
        decimalFormat.isParseBigDecimal = true
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        try {
            if (isUpdating) {
                isUpdating = false
                return
            }

            val text = s.toString().replace("\\D".toRegex(), "")
            if (text.isEmpty()) {
                editText.text.clear()
                return
            }

            val parsed = BigDecimal(text).setScale(2, BigDecimal.ROUND_FLOOR).divide(BigDecimal(100))
            val formatted = decimalFormat.format(parsed)

            isUpdating = true
            editText.setText(formatted)
            editText.setSelection(formatted.length)
        }catch (e:Exception){
            println("====== ERRO NO MoneyMaskWatcher =========== ${e.message} and ${e.cause} =================")
        }
    }
}
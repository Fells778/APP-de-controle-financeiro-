package com.example.app_controle_financeiro.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat
import java.util.Locale

object Helpers {
    fun removeCurrencyMask(value: String): Float {
        val cleanedValue = value.replace("\\D".toRegex(), "").trim()
        return cleanedValue.toFloat() / 100
    }


    fun formatCurrency(value: Float): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
    }

    fun removeDateMask(date: String): Int {
        val cleanedDate = date.replace("/", "")
        return cleanedDate.toInt()
    }

    // Esconder o teclado
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
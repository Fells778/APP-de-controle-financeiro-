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


    fun formatDate(date: Int): String {
        val dateString = date.toString()
        return "${dateString.substring(0..1)}/${dateString.substring(2..3)}/${dateString.substring(5..9)}"
    }

    // Esconder o teclado
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
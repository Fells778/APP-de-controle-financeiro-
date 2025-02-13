package com.example.app_controle_financeiro.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class DecimalValueFormatter : ValueFormatter() {
    private val decimalFormat = DecimalFormat("0.00") // 🔥 Define 2 casas decimais

    override fun getFormattedValue(value: Float): String {
        return "${decimalFormat.format(value)}%" // 🔥 Exibe os valores no formato "XX.XX%"
    }
}

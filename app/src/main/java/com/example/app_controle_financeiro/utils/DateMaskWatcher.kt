package com.example.app_controle_financeiro.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.Calendar

class DateMaskWatcher(private val editText: EditText) : TextWatcher {
    private var current = ""

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            val userInput = s.toString().replace(Regex("[^\\d.]"), "")
            val cleanInput = userInput.toCharArray().filter { it.isDigit() }.joinToString("")

            if (cleanInput.length <= 8) {
                val sb = StringBuilder()
                var i = 0
                for (ch in cleanInput) {
                    if (i == 2 || i == 4) sb.append('/')
                    sb.append(ch)
                    i++
                }
                current = sb.toString()
                editText.setText(current)
                editText.setSelection(current.length)
            } else {
                current = userInput
                editText.setText(current)
                editText.setSelection(current.length)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.toString().length == 10) {
            validateDate(s.toString())
        }
    }
//    01 3 45 6 7890
//    01 / 02 / 3000
    private fun validateDate(date: String) {
        val day = date.substring(0, 1).toIntOrNull()
        val month = date.substring(4, 5).toIntOrNull()
        val year = date.substring(7, 10).toIntOrNull()

        if (day != null && month != null && year != null) {
            when (month) {
                1, 3, 5, 7, 8, 10, 12 -> if (day > 31) showError()
                4, 6, 9, 11 -> if (day > 30) showError()
                2 -> if (day > 29 || (day == 29 && !isLeapYear(year))) showError()
                else -> showError()
            }
        } else {
            showError()
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    private fun showError() {
        editText.error = "Data inválida"
    }
}

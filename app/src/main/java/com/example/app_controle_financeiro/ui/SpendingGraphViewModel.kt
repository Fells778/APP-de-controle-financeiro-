package com.example.app_controle_financeiro.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_controle_financeiro.utils.Actions
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SpendingGraphViewModel(application: Application) : AndroidViewModel(application) {

    private val _actions = MutableLiveData<List<Actions>>()
    val actions: LiveData<List<Actions>> get() = _actions

    private val sharedPreferences =
        application.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    init {
        loadData()
    }

    fun loadData() {
        val json = sharedPreferences.getString("actionsList", null)
        if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Actions>>() {}.type
            _actions.value = gson.fromJson(json, type)
        } else {
            _actions.value = emptyList()
        }
    }

    fun addAction(action: Actions) {
        val updateList = _actions.value.orEmpty().toMutableList()
        updateList.add(action)
        // Notificando a UI
        _actions.postValue(updateList)
        // Salvando no SharedPreferences
        saveData(updateList)
    }

    private fun saveData(actions: List<Actions>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(actions)
        editor.putString("actionsList", json)
        editor.apply()
    }

    fun getPieChartData(): List<PieEntry> {
        val actionsList = _actions.value ?: emptyList()
        val totalSpending = actionsList.filter { it.action == "Gasto" }
            .mapNotNull { it.value }
            .sum()
        val totalInvestment = actionsList.filter { it.action == "Investimento" }
            .mapNotNull { it.value }
            .sum()

        val total = totalSpending + totalInvestment


        val spendingPercentage = if (total > 0) (totalSpending / total) * 100 else 0f
        val investmentPercentage = if (total > 0) (totalInvestment / total) * 100 else 0f

        return listOf(
            PieEntry(spendingPercentage, "Gastos"),
            PieEntry(investmentPercentage, "Investimentos")
        )
    }

    fun getTotalValues(): Pair<Float, Float> {
        val actionsList = _actions.value ?: emptyList()
        val totalSpent =
            actionsList.filter { it.action == "Gasto" }.sumOf { it.value?.toDouble() ?: 0.0 }
                .toFloat()
        val totalInvestment =
            actionsList.filter { it.action == "Investimento" }.sumOf { it.value?.toDouble() ?: 0.0 }
                .toFloat()

        return Pair(totalSpent, totalInvestment)
    }
}

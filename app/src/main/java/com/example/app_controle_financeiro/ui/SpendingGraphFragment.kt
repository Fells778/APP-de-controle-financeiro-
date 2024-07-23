package com.example.app_controle_financeiro.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app_controle_financeiro.databinding.FragmentSpendingGraphBinding
import com.example.app_controle_financeiro.utils.Actions
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SpendingGraphFragment : Fragment() {
    private lateinit var binding: FragmentSpendingGraphBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpendingGraphBinding.inflate(inflater, container, false)

        initViews()


        return binding.root
    }

    private fun initViews() {
        val actions = loadData(requireContext())
        saveData(requireContext(), actions)
        setupPieChart()
        loadPieChartData()
    }


    private fun setupPieChart() {
        binding.pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            centerText = "Gastos e Investimentos"
            setCenterTextSize(24f)
            description.isEnabled = false
            clickOnTheGraph()
        }
    }

    private fun clickOnTheGraph() {
        try {
            binding.pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(entry: Entry?, h: Highlight?) {
                    if (entry != null) {
                        when ((entry as PieEntry).label) {
                            "Gastos" -> {
                                findNavController().navigate(SpendingGraphFragmentDirections.actionSpendingGraphNavToOnlySpedingFragment())
                            }

                            "Investimentos" -> {
                                findNavController().navigate(SpendingGraphFragmentDirections.actionSpendingGraphNavToOnlyInvestmentFragment())
                            }
                        }
                    }
                }

                override fun onNothingSelected() {
                    println("=========")
                }

            })
        } catch (e: Exception) {
            println("================== ${e.message} ===========")
        }
    }

    private fun loadPieChartData() {
        val actions = loadData(requireContext())

        println("============= actions $actions")
        val totalSpending = actions.filter { it.action == "Gasto" }
            .mapNotNull { it.value }
            .sum()
        val totalInvestment = actions.filter { it.action == "Investimento" }
            .mapNotNull { it.value }
            .sum()

        val total = totalSpending + totalInvestment


        val spendingPercentage = (totalSpending / total) * 100
        val investmentPercentage = (totalInvestment / total) * 100

        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(spendingPercentage, "Gastos"))
        entries.add(PieEntry(investmentPercentage, "Investimentos"))

        println("========================== Grafico: $totalSpending & $totalInvestment & $total ================")
        val colors = listOf(
            Color.rgb(244, 67, 54), // Vermelho
            Color.rgb(76, 175, 80) // Verde
        )

        val dataSet = PieDataSet(entries, "Categorias")
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueTextSize(24f)
        data.setValueTextColor(Color.BLACK)

        binding.pieChart.apply {
            this.data = data
            invalidate() // Atualize o gráfico
            animateY(1000, Easing.EaseInOutCirc)
        }
    }

    private fun saveData(context: Context, actions: List<Actions>) {
        val sharedPreferences =
            context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(actions)

        editor.putString("actionsList", json)
        editor.apply()
    }

    private fun loadData(context: Context): List<Actions> {
        val sharedPreferences =
            context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("actionsList", null)

        if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Actions>>() {}.type
            return gson.fromJson(json, type)
        }

        return emptyList()
    }

}
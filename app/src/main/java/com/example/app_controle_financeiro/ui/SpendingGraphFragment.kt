package com.example.app_controle_financeiro.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.app_controle_financeiro.R
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
    private val viewModel: SpendingGraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpendingGraphBinding.inflate(inflater, container, false)

        initViews()


        return binding.root
    }

    private fun initViews() {
        setupPieChart()
        viewModel.actions.observe(viewLifecycleOwner) {
            loadPieChartData()
        }
    }


    private fun setupPieChart() {
        binding.pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(14f)
            setEntryLabelColor(Color.BLACK)
            centerText = context?.getString(R.string.string_spending_investment)
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
        val entries = viewModel.getPieChartData()
        val colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.color_spent),
            ContextCompat.getColor(requireContext(), R.color.color_investment)
        )

        val dataSet = PieDataSet(entries, getString(R.string.string_category)).apply {
            this.colors = colors
        }

        val data = PieData(dataSet).apply {
            setDrawValues(true)
            setValueTextSize(24f)
            setValueTextColor(Color.BLACK)
        }

        binding.pieChart.apply {
            this.data = data
            invalidate() // Atualize o gráfico
            animateY(1000, Easing.EaseInOutCirc)
        }
    }

}
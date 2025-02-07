package com.example.app_controle_financeiro.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_controle_financeiro.adapters.WallOfActionsAdapter
import com.example.app_controle_financeiro.databinding.FragmentOnlyInvestmentBinding
import com.example.app_controle_financeiro.utils.Actions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OnlyInvestmentFragment : Fragment() {
    private lateinit var binding: FragmentOnlyInvestmentBinding
    private lateinit var adapter: WallOfActionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlyInvestmentBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {
        val actions = loadData(requireContext())
        saveData(requireContext(), actions)
        initRecyclerView()
        loadingInvestment()
        backToGraph()
    }

    private fun initRecyclerView() {
        adapter = WallOfActionsAdapter()

        binding.recyclerViewOnlyInvestment.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = this@OnlyInvestmentFragment.adapter
        }
    }

    private fun loadingInvestment() {
        val actions = loadData(requireContext())
        val investmentList = actions.filter { it.action == "Investimento" }

        println("=========== Actions Loaded: ${actions.size} ===========")
        println("=========== SpendingList Size: ${investmentList.size} ===========")

        adapter.submitList(investmentList)
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

    private fun backToGraph(){
        binding.imageViewBack.setOnClickListener {
            findNavController().navigate(OnlyInvestmentFragmentDirections.actionOnlyInvestmentFragmentToSpendingGraphNav())
        }
    }

}
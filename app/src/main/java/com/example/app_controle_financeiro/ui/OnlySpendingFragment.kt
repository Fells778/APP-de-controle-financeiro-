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
import com.example.app_controle_financeiro.databinding.FragmentOnlySpendingBinding
import com.example.app_controle_financeiro.utils.Actions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OnlySpendingFragment : Fragment() {
    private lateinit var binding: FragmentOnlySpendingBinding
    private lateinit var adapter: WallOfActionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlySpendingBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {
        val actions = loadData(requireContext())
        saveData(requireContext(), actions)
        initRecyclerView()
        loadingSpending()
        backToGraph()
    }

    private fun initRecyclerView() {
        adapter = WallOfActionsAdapter()

        binding.recyclerViewOnlySpeding.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = this@OnlySpendingFragment.adapter
        }
    }

    private fun loadingSpending(){
        val actions = loadData(requireContext())
        val spendingList = actions.filter { it.action == "Gasto" }

        println("=========== Actions Loaded: ${actions.size} ===========")
        println("=========== SpendingList Size: ${spendingList.size} ===========")

        adapter.submitList(spendingList)
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
        binding.imageViewBackToGraph.setOnClickListener {
            findNavController().navigate(OnlySpendingFragmentDirections.actionOnlySpedingFragmentToSpendingGraphNav())
        }
    }

}
package com.example.app_controle_financeiro.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_controle_financeiro.adapters.WallOfActionsAdapter
import com.example.app_controle_financeiro.databinding.FragmentWallOfActionsBinding
import com.example.app_controle_financeiro.utils.Actions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WallOfActionsFragment : Fragment() {
    private lateinit var binding: FragmentWallOfActionsBinding
    private lateinit var adapterWall: WallOfActionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWallOfActionsBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {
        val actions = loadData(requireContext())
        saveData(requireContext(), actions)
        initRecyclerView()
        adapterWall.submitList(actions)
        initSpinnerOrder()
    }

    private fun initRecyclerView() {
        adapterWall = WallOfActionsAdapter()

        binding.recyclerViewWallOfActions.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = adapterWall
        }

    }

    private fun initSpinnerOrder() {
        binding.spinnerOrder.apply {
            val orderList = listOf("Ordernar por", "Data")
            val adapterOrders = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                orderList
            )
            adapterOrders.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = adapterOrders
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
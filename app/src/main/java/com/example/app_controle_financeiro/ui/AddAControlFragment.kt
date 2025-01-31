package com.example.app_controle_financeiro.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.app_controle_financeiro.R
import com.example.app_controle_financeiro.databinding.FragmentAddAControlBinding
import com.example.app_controle_financeiro.utils.Actions
import com.example.app_controle_financeiro.utils.DateMaskWatcher
import com.example.app_controle_financeiro.utils.Helpers
import com.example.app_controle_financeiro.utils.Helpers.removeCurrencyMask
import com.example.app_controle_financeiro.utils.Helpers.removeDateMask
import com.example.app_controle_financeiro.utils.MoneyMaskWatcher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddAControlFragment : Fragment() {
    private lateinit var binding: FragmentAddAControlBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAControlBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {
        addControlList()
        initSpinnerAction()
        buttonClearFields()
        initMasks()
        hideKeyboard()
    }
// Começar a fazer o darkmode e olhar pq tá com o nome categorias no grafico...
    private fun initMasks() {
        binding.apply {
            editTextValue.addTextChangedListener(MoneyMaskWatcher(editTextValue))
            editTextDay.addTextChangedListener(DateMaskWatcher(editTextDay))
        }
    }

    private fun addControlList() {
        binding.apply {
            buttonAddControl.setOnClickListener {
                if (verificationFields()) {
                    passingDataToTheWall()
                    clearFields()
                    loading()
                }
                hideKeyboard()
            }
        }
    }

    private fun passingDataToTheWall() {
        val action = binding.spinnerAction.selectedItem.toString()
        val value = binding.editTextValue.text.toString()
        val date = binding.editTextDay.text.toString()
        var type = binding.spinnerType.selectedItem.toString()
        var description = binding.editTextDescriptionAdd.text.toString()

        val emptyType = listOf("Investimento")
        if (type.isEmpty()) {
            type = emptyType.toString()
        }

        description = if (description.isEmpty()) {
            "Null em description"
        } else {
            binding.editTextDescriptionAdd.text.toString()
        }

        addAction(action, type, description, value, date)
        println("============== passingDataToTheWall: $action, $type, $description, $value, $date")

    }

    private fun addAction(
        action: String,
        type: String,
        description: String,
        value: String,
        date: String
    ) {
        try {
            val valueString = removeCurrencyMask(value)
            val dateString = removeDateMask(date)

            val newAction = Actions(action, type, description, valueString, dateString)

            val actionList = loadData().toMutableList()
            actionList.add(newAction)
            saveData(actionList)
        } catch (e: NumberFormatException) {
            println("Error parsing number: ${e.message}")
        }
    }


    private fun saveData(actions: List<Actions>) {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(actions)

        editor.putString("actionsList", json)
        editor.apply()
    }


    private fun loadData(): List<Actions> {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("actionsList", null)

        if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Actions>>() {}.type
            return gson.fromJson(json, type)
        }

        return emptyList()
    }

    private fun verificationFields(): Boolean {
        binding.apply {
            if (spinnerAction.selectedItem.toString() == "Selecione uma ação") {
                imageViewErrorSpinnerAction.isVisible = true
                Toast.makeText(context, "Selecione uma ação", Toast.LENGTH_SHORT).show()
                return false
            }

            if (spinnerAction.selectedItem.toString() == "Gasto" && spinnerType.selectedItem.toString() == "Selecione o tipo") {
                imageViewErrorSpinnerType.isVisible = true
                Toast.makeText(context, "Selecione o tipo", Toast.LENGTH_SHORT).show()
                return false
            }
            if (editTextValue.text!!.isEmpty()) {
                editTextValue.requestFocus()
                editTextValue.error = "Informe o valor"
                return false
            }
            if (editTextDay.text!!.isEmpty()) {
                editTextDay.requestFocus()
                editTextDay.error = "Informe o dia"
                return false
            }
            if (editTextDay.text!!.length < 10) {
                editTextDay.requestFocus()
                editTextDay.error = "Informe uma data valida"
                return false
            }

            imageViewErrorSpinnerAction.isVisible = false
            imageViewErrorSpinnerType.isVisible = false
            return true
        }
    }

    private fun initSpinnerAction() {
        binding.apply {
            val actionsList = listOf("Selecione uma ação", "Gasto", "Investimento")

            val adapterActions = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                actionsList
            )
            adapterActions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAction.adapter = adapterActions

            spinnerAction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Manipular a seleção da ação aqui
                    when (actionsList[position]) {
                        "Selecione uma ação" -> {
                            linearLayoutDescriptionAdd.isVisible = false
                            spinnerType.isVisible = false
                            textView6.isVisible = false
                            imageViewErrorSpinnerAction.isVisible = false
                            imageViewErrorSpinnerType.isVisible = false
                        }

                        "Gasto" -> {
                            initSpinnerType()
                            spinnerType.isVisible = true
                            textView6.isVisible = true
                            imageViewErrorSpinnerType.isVisible = false
                            linearLayoutDescriptionAdd.isVisible = false
                        }

                        "Investimento" -> {
                            spinnerType.isVisible = false
                            textView6.isVisible = false
                            imageViewErrorSpinnerType.isVisible = false
                            linearLayoutDescriptionAdd.isVisible = true
                        }

                        else -> {
                            linearLayoutDescriptionAdd.isVisible = false
                            spinnerType.isVisible = false
                            textView6.isVisible = false
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Ação quando nada é selecionado
                }

            }

        }
    }

    private fun initSpinnerType() {
        val typeList = listOf(
            "Selecione o tipo",
            "Delivery",
            "Internet",
            "Mercado",
            "Compras online",
            "Água",
            "Luz",
            "Gasolina",
            "Academia",
            "Aluguel",
            "Festa/rolé",
            "Outros"
        )
        val adapterTypes =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, typeList)
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapterTypes

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val typeSelect = typeList[position]
                if (typeSelect != "Selecione o tipo") {
                    binding.imageViewErrorSpinnerType.isVisible = false
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun buttonClearFields() {
        binding.buttonDeleteControl.setOnClickListener {
            clearFields()
            hideKeyboard()
        }
    }


    private fun clearFields() {
        binding.apply {
            editTextValue.text?.clear()
            editTextDay.text?.clear()
        }
    }

    private fun hideKeyboard() {
        val inputMethodMenage =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(activity)
        inputMethodMenage.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun loading() {
        val dialog = Dialog(requireContext())
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            Toast.makeText(context, R.string.string_added_to_the_wall, Toast.LENGTH_SHORT).show()
        }, 500)
        dialog.setContentView(R.layout.custom_dialog_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun dataIsValid() {
        val date = binding.editTextDay.text


    }

}
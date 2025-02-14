package com.example.app_controle_financeiro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_controle_financeiro.R
import com.example.app_controle_financeiro.databinding.CustomLayoutWallOfActionsBinding
import com.example.app_controle_financeiro.utils.Actions
import com.example.app_controle_financeiro.utils.Helpers.formatCurrency
import java.text.SimpleDateFormat
import java.util.Locale

class WallOfActionsAdapter :
    ListAdapter<Actions, WallOfActionsAdapter.MainViewHolder>(ActionsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = CustomLayoutWallOfActionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<Actions>?) {
        val sortedList = list?.sortedBy { it.date }
        super.submitList(sortedList)
    }

    inner class MainViewHolder(binding: CustomLayoutWallOfActionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val action = binding.textViewActionCustomLayout
        private val type = binding.textViewTypeCustomLayout
        private val description = binding.textViewDescriptionCustomLayout
        private val values = binding.textViewValueCustomLayout
        private val date = binding.textViewDateCustomLayout
        private val viewColor = binding.viewColor

        fun bind(actions: Actions) {
            itemView.setOnClickListener {
                println(date.text)
                println(actions.date)
            }
            action.text = actions.action
            type.text = actions.type
            values.text = formatCurrency(actions.value ?: 0f)
            description.text = actions.description

            date.text = formatDate(actions.date.toString())

            type.isVisible = actions.action == "Gasto"
            description.isVisible = actions.action == "Investimento"

            val colorsMap = mapOf(
                "Gasto" to R.color.color_spent,
                "Investimento" to R.color.color_investment
            )

            val color = colorsMap[actions.action] ?: R.color.default_color_text
            viewColor.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
        }
    }

    fun formatDate(date: String): String {
        return try {
            when (date.length) {
                8 -> {
                    // Se já estiver no formato correto (DDMMAAAA), faz o split corretamente
                    val formattedDate = "${date.substring(0, 2)}/${date.substring(2, 4)}/${date.substring(4, 8)}"
                    formattedDate
                }
                7 -> {
                    // Se faltar um zero no início, adicionamos manualmente
                    val formattedDate = "0${date.substring(0, 1)}/${date.substring(1, 3)}/${date.substring(3, 7)}"
                    formattedDate
                }
                else -> {
                    "Data Inválida"
                }
            }
        } catch (e: Exception) {
            "Erro ao formatar"
        }
    }



    class ActionsDiffCallback : DiffUtil.ItemCallback<Actions>() {
        override fun areItemsTheSame(oldItem: Actions, newItem: Actions): Boolean {
            // Identificar se dois itens são os mesmos (ex: comparando ID)
            return oldItem.date == newItem.date && oldItem.value == newItem.value
        }

        override fun areContentsTheSame(oldItem: Actions, newItem: Actions): Boolean {
            // Verificar se os conteúdos dos itens são os mesmos
            return oldItem == newItem
        }

    }
}
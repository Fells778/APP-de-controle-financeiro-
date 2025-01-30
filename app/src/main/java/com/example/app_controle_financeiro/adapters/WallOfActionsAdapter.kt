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
            }
            action.text = actions.action
            type.text = actions.type
            values.text = formatCurrency(actions.value ?: 0f)
            description.text = actions.description
            try {
                val formatDate = actions.date.toString()
                if (formatDate.length.toString().toInt() == 8) {
                    val aaa = "${formatDate.substring(0..1)}/${formatDate.substring(2..3)}/${formatDate.substring(4..7)}"
                    date.text = aaa
                }else if (formatDate.length.toString().toInt() < 8){
                    val dataWithZeroStart = "0${formatDate.substring(0..0)}/${formatDate.substring(1..2)}/${formatDate.substring(3..6)}"
                    date.text = dataWithZeroStart
                }
            } catch (e: Exception) {
                println("=========== catch adapter ${e.cause} e ${actions.date.toString()}")
            }

            when (actions.action) {
                "Gasto" -> {
                    description.isVisible = false
                    type.isVisible = true
                }

                "Investimento" -> {
                    description.isVisible = true
                    type.isVisible = false
                }

                else -> {
                    description.isVisible = false
                    type.isVisible = false
                }
            }

            if (actions.action == "Gasto") {
                viewColor.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_spent
                    )
                )
            } else {
                viewColor.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_investment
                    )
                )
            }
        }
    }

    class ActionsDiffCallback : DiffUtil.ItemCallback<Actions>() {
        override fun areItemsTheSame(oldItem: Actions, newItem: Actions): Boolean {
            // Identificar se dois itens são os mesmos (ex: comparando ID)
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Actions, newItem: Actions): Boolean {
            // Verificar se os conteúdos dos itens são os mesmos
            return oldItem == newItem
        }

    }
}
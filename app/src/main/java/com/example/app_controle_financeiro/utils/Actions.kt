package com.example.app_controle_financeiro.utils

import java.io.Serializable

data class Actions(
    var action: String? = null,
    val type: String? = null,
    val description: String? = null,
    val value: Float? = null,
    val date: Int? = null
) : Serializable
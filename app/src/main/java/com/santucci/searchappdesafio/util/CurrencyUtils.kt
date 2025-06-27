package com.santucci.searchappdesafio.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val localeMap = mapOf(
        "ARS" to Locale("es", "AR"),
        "BRL" to Locale("pt", "BR"),
        "USD" to Locale.US
    )

    private fun getLocaleForCurrency(currencyId: String): Locale =
        localeMap[currencyId.uppercase()] ?: Locale.getDefault()

    fun formatCurrency(value: Double, currencyId: String): String {
        val locale = getLocaleForCurrency(currencyId)
        val formatter = NumberFormat.getCurrencyInstance(locale).apply {
            maximumFractionDigits = if (value % 1.0 == 0.0) 0 else 2
        }
        return formatter.format(value)
    }
}
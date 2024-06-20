package com.cpp.unsmoke.utils.helper.gamification

import java.text.DecimalFormat

object Gamification {
    const val RELAPSE_REWARD = 10
    const val JOURNAL_REWARD = 5
    const val BREATH_REWARD = 5

    fun formatNumber(value: Double): String {
        val suffixes = arrayOf("", "K", "M", "B", "T")
        val formatter = DecimalFormat("#.#")

        var formattedValue = value
        var suffixIndex = 0

        while (formattedValue >= 1000 && suffixIndex < suffixes.size - 1) {
            formattedValue /= 1000
            suffixIndex++
        }

        return formatter.format(formattedValue) + suffixes[suffixIndex]
    }

}
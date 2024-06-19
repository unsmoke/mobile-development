package com.cpp.unsmoke.utils.helper.date

import java.text.SimpleDateFormat

object Date {
    fun getEpochTimeNow(): Long {
        return System.currentTimeMillis() / 1000
    }

    fun getDateFromEpoch(epoch: Long): String {
        val date = java.util.Date(epoch * 1000)
        val sdf = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        return sdf.format(date)
    }
}
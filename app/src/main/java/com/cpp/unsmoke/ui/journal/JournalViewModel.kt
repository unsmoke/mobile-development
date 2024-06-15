package com.cpp.unsmoke.ui.journal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.repository.JournalRepository

class JournalViewModel(private val journalRepository: JournalRepository): ViewModel() {

    /* FRAGMENT TWO */
    private val _feeling = MutableLiveData<String>()
    val feeling: LiveData<String> get() = _feeling

    /* FRAGMENT THREE */
    private val _challenge = MutableLiveData<String>()
    val challenge: LiveData<String> get() = _challenge

    /* FRAGMENT FOUR */
    private val _commitment = MutableLiveData<String>()
    val commitment: LiveData<String> get() = _commitment

    fun setFeeling(feeling: String) {
        _feeling.value = feeling
    }

    fun setChallenge(challenge: String) {
        _challenge.value = challenge
    }

    fun setCommitment(commitment: String) {
        _commitment.value = commitment
    }

    fun getAllData() {
        val allData = """
            Feeling: ${feeling.value}
            Challenge: ${challenge.value}
            Commitment: ${commitment.value}
        """.trimIndent()

        Log.d("JournalViewModel", allData)
    }


}
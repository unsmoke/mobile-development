package com.cpp.unsmoke.ui.journal.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.cpp.unsmoke.BuildConfig
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentJournalingFiveBinding
import com.cpp.unsmoke.ui.journal.JournalViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class JournalingFiveFragment : Fragment() {
    private var _binding: FragmentJournalingFiveBinding? = null
    private val binding get() = _binding!!

    private var commitmentValue = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJournalingFiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val journalingViewModel = ObtainViewModelFactory.obtainAuth<JournalViewModel>(requireActivity())

        journalingViewModel.feeling.observe(viewLifecycleOwner) { feeling ->
            binding.tvFeeling.text = getString(R.string.summary_emotions, feeling)
        }

        journalingViewModel.challenge.observe(viewLifecycleOwner) { challenge ->
            binding.tvChallenge.text =
                getString(R.string.summary_challenges, challenge)
        }

        journalingViewModel.commitment.observe(viewLifecycleOwner) { commitment ->
            binding.tvCommitment.text =
                getString(R.string.summary_commitment, commitment)
                commitmentValue = commitment
        }

        binding.btnNext.setOnClickListener {
            requireActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                requireActivity().finish()
            }
        })

        requireActivity()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            loadGeminiResponse()
        }
    }

    private suspend fun loadGeminiResponse() {

        val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)

        val hateSpeechSafety = SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH)

        val sexualExplicit = SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH)

        val dangerous = SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH)

        val generationConfig = generationConfig {
            responseMimeType = "application/json"
            temperature = 0.9f
            topK = 16
            topP = 0.1f
        }

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.GEMINI_API,
            generationConfig = generationConfig,
            safetySettings = listOf(harassmentSafety, hateSpeechSafety, sexualExplicit, dangerous)
        )

        val prompt = """
            KOMITMENT

            Saya memiliki sebuah journaling yang saya tulis mengenai komitment saya terhadap perjalanan saya dalam berhenti merokok. Adapun journaling komitment saya pada hari ini adalah sebabagai berikut: 

            "Saya ingin berhenti merokok karena saya ${commitmentValue}".
             
            Tolong buatkan pesan motivasi berdasarkan journaling di atas yang akan saya tampilkan di notifikasi hp saya setiap 6 jam sekali. Adapun tolong berikan dalam bentuk JSON Array yang berisi objek yang terdiri dari id dan pesannya.
        """.trimIndent()
        val response = generativeModel.generateContent(prompt)
        Log.d("GeminiTextResponse", "Response: ${response.text}")
        Log.d("GeminiRawResponse", "Response: $response")
        response.text?.let { jsonString ->
            try {
                val jsonArray = JSONArray(jsonString)
                val messages = mutableListOf<Pair<String, String>>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getString("id")
                    val message = jsonObject.getString("pesan")
                    messages.add(id to message)
                }
                for ((id, message) in messages) {
                    Log.d("Gemini", "ID: $id, Message: $message")
                }
            } catch (e: Exception) {
                Log.e("Gemini", "Error parsing JSON response", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
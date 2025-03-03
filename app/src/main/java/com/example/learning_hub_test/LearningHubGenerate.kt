package com.example.learning_hub_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LearningHubGenerate : AppCompatActivity() {
    private lateinit var model: GenerativeModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_hub_generate)

        val etPrompt = findViewById<EditText>(R.id.editTextPrompt)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        progressBar = findViewById(R.id.progressBar)

        // Initialize Gemini API
        model = GenerativeModel("gemini-2.0-flash-exp", "AIzaSyDbK7-F880keq81ULnnAw6ZSH5GDd4XeEA")

        btnGenerate.setOnClickListener {
            val prompt = etPrompt.text.toString().trim()
            if (prompt.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                generateLesson(prompt)
            } else {
                Toast.makeText(this, "Please enter a title of the lesson you want to learn.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateLesson(prompt: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val chat = model.startChat(
                    listOf(content("user") {
                        text("imagine you're a feature on an app called Sexactly that helps people learn. " +
                                "a feature called LearningHub: Interactive Modules: Courses on sexual health topics generated through A.I. " +
                                "Create a lesson for me to study about: $prompt. " +
                                "The lesson should be long but understandable and include a title, description/objectives, useful content, and references. " +
                                "In generating, you should proceed to the lesson title (in bold text) no need to say Okay, I can create a blah blah." +
                                "make sure you are only providing accurate and correct information with reliable references and sources" +
                                "put this disclaimer at the end of the lesson: Disclaimer: This module is for informational purposes only and does not " +
                                "constitute medical advice. Please consult with a qualified healthcare professional for any health concerns or before making " +
                                "any decisions related to your health or treatment.")
                    })
                )

                val response = chat.sendMessage(prompt)

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    val generatedLesson = response.text ?: "No lesson generated."

                    // Navigate to the overview page with the generated lesson
                    val intent = Intent(this@LearningHubGenerate, LearningHubOverview::class.java)
                    intent.putExtra("lessonContent", generatedLesson)
                    intent.putExtra("lessonPrompt", prompt)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LearningHubGenerate, "Error generating lesson: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

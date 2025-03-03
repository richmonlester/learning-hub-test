package com.example.learning_hub_test

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LearningHubOverview : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_hub_overview)

        db = FirebaseFirestore.getInstance()
        val tvLesson = findViewById<TextView>(R.id.textViewLesson)
        val btnEdit = findViewById<Button>(R.id.btnEditPrompt)
        val btnSave = findViewById<Button>(R.id.btnSaveLesson)

        val lessonContent = intent.getStringExtra("lessonContent") ?: ""
        val lessonPrompt = intent.getStringExtra("lessonPrompt") ?: ""

        tvLesson.text = lessonContent

        val formattedText = parseMarkdownToHtml(lessonContent)
        tvLesson.text = formattedText

        // Handle Edit Button
        btnEdit.setOnClickListener {
            val intent = Intent(this, LearningHubGenerate::class.java)
            intent.putExtra("lessonPrompt", lessonPrompt) // Pass back prompt for editing
            startActivity(intent)
            finish() // Close current activity
        }

        // Handle Save Button
        btnSave.setOnClickListener {
            val lesson = hashMapOf("title" to lessonPrompt, "content" to lessonContent)

            db.collection("lessons").add(lesson)
                .addOnSuccessListener {
                    Toast.makeText(this, "Lesson saved successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LearningHubMain::class.java))
                    finish() // Close current activity
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving lesson: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun parseMarkdownToHtml(text: String): Spanned {
        val htmlText = text
            .replace("\\*\\*(.*?)\\*\\*".toRegex(), "<b>$1</b>") // Convert **bold** to <b>bold</b>
            .replace("\n\n", "<br><br>") // Preserve paragraph spacing
            .replace("\n", "<br>") // Ensure single line breaks are respected

        return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    }
}

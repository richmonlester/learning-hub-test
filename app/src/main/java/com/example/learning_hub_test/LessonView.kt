package com.example.learning_hub_test

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LessonView : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private var lessonId: String? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: ScrollView
    private lateinit var tvLessonContent: TextView
    private var totalScrollHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_view)

        db = FirebaseFirestore.getInstance()
        tvLessonContent = findViewById(R.id.tvLessonContent)
        progressBar = findViewById(R.id.progressBarLesson)
        scrollView = findViewById(R.id.scrollViewLesson)
        val btnDone = findViewById<Button>(R.id.btnDoneLearning)

        lessonId = intent.getStringExtra("lessonId")

        lessonId?.let { id ->
            db.collection("lessons").document(id).get().addOnSuccessListener { document ->
                val rawContent = document.getString("content") ?: ""
                val formattedText = parseMarkdownToHtml(rawContent)
                tvLessonContent.text = formattedText

                // Restore progress
                val savedProgress = document.getLong("progress")?.toInt() ?: 0
                progressBar.progress = savedProgress
                scrollView.post {
                    val scrollY = (scrollView.getChildAt(0).height * savedProgress) / 100
                    scrollView.scrollTo(0, scrollY)
                }
            }
        }

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (scrollView.getChildAt(0) != null) {
                totalScrollHeight = scrollView.getChildAt(0).height - scrollView.height
                val scrolledPercentage = (scrollView.scrollY * 100) / totalScrollHeight
                progressBar.progress = scrolledPercentage

                lessonId?.let { id ->
                    db.collection("lessons").document(id)
                        .update("progress", scrolledPercentage)
                }
            }
        }

        btnDone.setOnClickListener {
            lessonId?.let { id ->
                db.collection("lessons").document(id).delete().addOnSuccessListener {
                    Toast.makeText(this, "Lesson Completed!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun parseMarkdownToHtml(text: String): Spanned {
        val htmlText = text
            .replace("\\*\\*(.*?)\\*\\*".toRegex(), "<b>$1</b>") // Convert **bold** to <b>bold</b>
            .replace("\n\n", "<br><br>") // Preserve paragraph spacing
            .replace("\n", "<br>") // Ensure line breaks are respected

        return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    }
}

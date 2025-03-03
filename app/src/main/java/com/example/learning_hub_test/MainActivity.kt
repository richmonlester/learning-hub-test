package com.example.learning_hub_test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class LearningHubMain : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var lessonAdapter: LessonAdapter
    private val lessonList = mutableListOf<Lesson>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_hub_main)

        db = FirebaseFirestore.getInstance()
        val btnGenerateLesson = findViewById<Button>(R.id.btnGenerateLesson)
        val rvLessons = findViewById<RecyclerView>(R.id.rvLessons)

        btnGenerateLesson.setOnClickListener {
            startActivity(Intent(this, LearningHubGenerate::class.java))
        }

        lessonAdapter = LessonAdapter(lessonList) { lesson ->
            val intent = Intent(this, LessonView::class.java)
            intent.putExtra("lessonId", lesson.id)
            startActivity(intent)
        }

        rvLessons.apply {
            adapter = lessonAdapter
            layoutManager = LinearLayoutManager(this@LearningHubMain)
            setHasFixedSize(true) // Optimizes performance
        }

        fetchLessons()
    }

    private fun fetchLessons() {
        db.collection("lessons").get().addOnSuccessListener { documents ->
            lessonList.clear()
            for (document in documents) {
                val lesson = Lesson(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    content = document.getString("content") ?: "",
                    progress = document.getLong("progress")?.toInt() ?: 0 // Ensure progress is fetched
                )
                lessonList.add(lesson)
            }
            lessonAdapter.notifyDataSetChanged() // Refresh UI with progress updates
        }
    }

    override fun onResume() {
        super.onResume()
        fetchLessons() // Auto-refresh list when returning
    }
}
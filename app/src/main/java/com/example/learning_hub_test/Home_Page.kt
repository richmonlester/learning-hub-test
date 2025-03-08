package com.example.learning_hub_test

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.util.Log
import android.widget.Toast

class Home_Page : AppCompatActivity() {
   @SuppressLint("MissingInflatedId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        val greetingTextView = findViewById<TextView>(R.id.greeting)
        val fullText = "Hello, Hezrone!"
        val spannableString = SpannableString(fullText)
        val blueColor = ContextCompat.getColor(this, R.color.blue)
        val darkPinkColor = ContextCompat.getColor(this, R.color.darkpink)

        val helloColorSpan = ForegroundColorSpan(darkPinkColor)
        spannableString.setSpan(helloColorSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val nameColorSpan = ForegroundColorSpan(blueColor)
        spannableString.setSpan(nameColorSpan, 7, fullText.length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        greetingTextView.text = spannableString

        val toLearningHub = findViewById<ImageView>(R.id.learningHub)
        toLearningHub.setOnClickListener {
            val intent = Intent(this, LearningHubMain::class.java)
            startActivity(intent)
            //new code



        /*
        val toSupport = findViewById<ImageView>(R.id.support)
        toSupport.setOnClickListener {
            val intent = Intent(this, SupportMaps::class.java)
            startActivity(intent)
        }
        val toSafeSpace = findViewById<ImageView>(R.id.safeSpace)
        toSafeSpace.setOnClickListener {
            val intent = Intent(this, Safe_Space::class.java)
            startActivity(intent)
        }
        val toAI = findViewById<ImageView>(R.id.chatbot_bttn)
        toAI.setOnClickListener {
            val intent = Intent(this, ChatBot::class.java)
            startActivity(intent)
        }
        val toHome = findViewById<ImageView>(R.id.home_bttn)
        toHome.setOnClickListener {
            val intent = Intent(this, Home_Page::class.java)
            startActivity(intent)
        }
        val toProfile = findViewById<ImageView>(R.id.profile_bttn)
        toProfile.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        val toRaya = findViewById<Button>(R.id.askRaya)
        toRaya.setOnClickListener {
            val intent = Intent(this, ChatBot::class.java)
            startActivity(intent)
        }

        */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    }
}
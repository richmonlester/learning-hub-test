package com.example.learning_hub_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class LessonAdapter(
    private val lessonList: List<Lesson>,
    private val onItemClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_title_card, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessonList[position]
        holder.tvTitle.text = lesson.title
        holder.progressBar.progress = lesson.progress // Ensure progress is assigned

        holder.cardView.setOnClickListener { onItemClick(lesson) }
    }



    override fun getItemCount(): Int = lessonList.size
}

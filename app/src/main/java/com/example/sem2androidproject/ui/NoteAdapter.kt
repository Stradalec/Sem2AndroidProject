package com.example.sem2androidproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sem2androidproject.domain.model.NoteModel
import com.example.sem2androidproject.R
import com.example.sem2androidproject.data.local.Category
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private var notes: List<NoteModel> = emptyList(),
    private var categories: Map<Long, Category> = emptyMap(),
    private val onDeleteClick: (NoteModel) -> Unit,
    private val onEditClick: (NoteModel) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.noteTitleTextView)
        val bodyTextView: TextView = itemView.findViewById(R.id.noteBodyTextView)
        val noteCategoryTextView: TextView = itemView.findViewById(R.id.noteCategory)
        val noteDateTextView: TextView = itemView.findViewById(R.id.noteDateTextView)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.amount.toString()
        holder.noteCategoryTextView.text = categories[note.categoryId]?.name ?: "—"
        holder.bodyTextView.text = note.noteBody
        holder.noteDateTextView.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .format(Date(note.noteDate))
        holder.deleteButton.setOnClickListener { onDeleteClick(note) }
        holder.editButton.setOnClickListener { onEditClick(note)}
    }

    fun updateNotes(newNotes: List<NoteModel>, newCategories: Map<Long, Category>) {
        notes = newNotes
        categories = newCategories
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}
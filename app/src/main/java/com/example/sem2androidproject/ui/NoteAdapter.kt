package com.example.sem2androidproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sem2androidproject.domain.model.NoteModel
import com.example.sem2androidproject.R
import java.util.Date

class NoteAdapter(
    private var notes: List<NoteModel> = emptyList(),
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
        holder.bodyTextView.text = note.noteBody
        holder.noteCategoryTextView.text = note.category
        holder.noteDateTextView.text = Date(note.noteDate).toString()
        holder.deleteButton.setOnClickListener { onDeleteClick(note) }
        holder.editButton.setOnClickListener { onEditClick(note)}
    }

    fun updateNotes(newNotes: List<NoteModel>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}
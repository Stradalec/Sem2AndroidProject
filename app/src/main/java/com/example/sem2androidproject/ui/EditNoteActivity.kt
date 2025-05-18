package com.example.sem2androidproject.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.sem2androidproject.R
import com.example.sem2androidproject.domain.ReminderWorker
import com.example.sem2androidproject.domain.model.NoteModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Double
import java.util.Date
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        val note = intent.getSerializableExtra("note") as? NoteModel
        if (note == null) {
            Toast.makeText(this, "Ошибка загрузки заметки", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        findViewById<EditText>(R.id.editAmountEditText).setText(note.amount.toString())
        findViewById<EditText>(R.id.editCategoryEditText).setText(note.category)
        findViewById<EditText>(R.id.editContentEditText).setText(note.noteBody)



        findViewById<Button>(R.id.btnSaveNotes).setOnClickListener {
            val newAmount = findViewById<EditText>(R.id.editAmountEditText).text.toString()
            val newCategory = findViewById<EditText>(R.id.editCategoryEditText).text.toString()
            val newContent = findViewById<EditText>(R.id.editContentEditText).text.toString()
            val amount = try {
                newAmount.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Некорректная сумма", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newAmount.isNotBlank() && newCategory.isNotBlank() && newContent.isNotBlank()) {
                val updatedNote = note.copy(
                    amount = amount,
                    category = newCategory,
                    noteBody = newContent,
                    noteDate = note.noteDate
                )
                if (updatedNote.reminderTime != null) {
                    val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                        .setInputData(workDataOf("NOTE_ID" to updatedNote.id))
                        .setInitialDelay(updatedNote.reminderTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .build()
                    WorkManager.getInstance(this).enqueue(workRequest)
                }
                viewModel.updateNote(updatedNote)
                finish()
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

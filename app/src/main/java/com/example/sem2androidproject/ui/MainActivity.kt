package com.example.sem2androidproject.ui

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sem2androidproject.domain.model.NoteModel
import com.example.sem2androidproject.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private lateinit var calendarDate: TextView
    private var globalSelectedDate: Date = Date()
    private var globalReminderTime: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialiseRecyclerView()
        observeNotesList()
        getNotes()
        setupAddNoteButton()
        calendarDate = findViewById(R.id.dateTextView)
        calendarDate.setOnClickListener{
            showDatePicker()
        }
        findViewById<Button>(R.id.btnSetReminder).setOnClickListener {
            showTimePicker()
        }
    }
    private fun initialiseRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rView)
        adapter = NoteAdapter(onDeleteClick = { noteModel: NoteModel ->
            viewModel.deleteNote(noteModel)
        }, onEditClick = { noteModel: NoteModel ->
            val intent = Intent(this@MainActivity, EditNoteActivity::class.java).apply {
                putExtra("note", noteModel as Serializable)
                Log.e("Edit", "Trying to start")
            }
            startActivity(intent)
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeNotesList() {
        viewModel.notes.observe(this) { notes ->
            notes?.let {
                adapter.updateNotes(it)
            }
        }
    }

    private fun getNotes() {
        viewModel.loadNotes()
    }

    private fun setupAddNoteButton() {
        findViewById<Button>(R.id.btnGetNotes).setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val content = findViewById<EditText>(R.id.contentEditText).text.toString()
            val category: String = findViewById<EditText>(R.id.categoryEditText).text.toString()
            if (title.isNotBlank() && content.isNotBlank() && category.isNotBlank()) {
                viewModel.addNote(NoteModel(title = title, category = category, noteBody = content, noteDate = globalSelectedDate.time, reminderTime = globalReminderTime.timeInMillis))
                clearInputFields()
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearInputFields() {
        findViewById<EditText>(R.id.titleEditText).text.clear()
        findViewById<EditText>(R.id.contentEditText).text.clear()
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                calendarDate.text = formattedDate

                val selectedDate = getDateFromCalendar(selectedYear, selectedMonth, selectedDay)
                globalSelectedDate.time = selectedDate.time
            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
    }

    private fun getDateFromCalendar(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        return calendar.time
    }
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                globalReminderTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                Toast.makeText(
                    this@MainActivity,
                    "Напоминание установлено на: ${hour}:${minute}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
    override fun onResume() {
        super.onResume()
        viewModel.loadNotes()
    }
}


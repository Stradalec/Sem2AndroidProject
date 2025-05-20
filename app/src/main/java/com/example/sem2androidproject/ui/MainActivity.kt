package com.example.sem2androidproject.ui

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sem2androidproject.domain.model.NoteModel
import com.example.sem2androidproject.R
import com.example.sem2androidproject.data.local.Category
import com.example.sem2androidproject.data.local.EntryType
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
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var currentCategories: List<Category> = emptyList()
    private lateinit var filterCategorySpinner: Spinner
    private lateinit var sortSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initialiseRecyclerView()
        observeCategories()
        observeNotesList()
        getNotes()
        setupAddNoteButton()
        setupCategorySpinner()
        filterCategorySpinner = findViewById(R.id.filterCategorySpinner)
        sortSpinner = findViewById(R.id.sortSpinner)
        setupFilterSpinner()
        categoryViewModel.loadCategoriesByType(EntryType.EXPENSE)
        calendarDate = findViewById(R.id.dateTextView)
        calendarDate.setOnClickListener{
            showDatePicker()
        }
        findViewById<Button>(R.id.btnSetReminder).setOnClickListener {
            showTimePicker()
        }
        findViewById<Button>(R.id.btnManageCategories).setOnClickListener {
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
        }
        findViewById<Button>(R.id.btnStatistics).setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
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
        }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun observeNotesList() {
        viewModel.notes.observe(this) { notes ->
            val categoryMap = currentCategories.associateBy { it.id }
            adapter.updateNotes(notes, categoryMap)
            notes.forEach { note ->
                Log.d("Notes", "Note ID: ${note.id}, Category ID: ${note.categoryId}")
            }
        }
    }

    private fun observeCategories() {
        categoryViewModel.categories.observe(this) { categories ->
            currentCategories = categories
            val notes = viewModel.notes.value ?: emptyList()
            val categoryMap = currentCategories.associateBy { it.id }
            adapter.updateNotes(notes, categoryMap)
        }
    }


    private fun getNotes() {
        viewModel.loadNotes()
    }

    private fun setupAddNoteButton() {
        findViewById<Button>(R.id.btnGetNotes).setOnClickListener {
            val amountText = findViewById<EditText>(R.id.amountEditText).text.toString()
            val content = findViewById<EditText>(R.id.contentEditText).text.toString()
            val spinner = findViewById<Spinner>(R.id.categorySpinner)
            val selectedCategory = spinner.selectedItem as? Category
            Log.e("smert", selectedCategory.toString())
            val type = when (findViewById<RadioGroup>(R.id.typeRadioGroup).checkedRadioButtonId) {
                R.id.expenseRadio -> EntryType.EXPENSE
                else -> EntryType.INCOME
            }

            if (amountText.isBlank() || content.isBlank() || selectedCategory == null) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = try {
                amountText.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Некорректная сумма", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addNote(
                NoteModel(
                    amount = amount,
                    noteBody = content,
                    noteDate = globalSelectedDate.time,
                    reminderTime = globalReminderTime.timeInMillis,
                    categoryId = selectedCategory.id,
                    type = type
                )
            )
            clearInputFields()
        }
    }


    private fun clearInputFields() {
        findViewById<EditText>(R.id.amountEditText).text.clear()
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

    private fun setupCategorySpinner() {
        val spinner = findViewById<Spinner>(R.id.categorySpinner)
        val radioGroup = findViewById<RadioGroup>(R.id.typeRadioGroup)

        val adapter = ArrayAdapter<Category>(
            this,
            android.R.layout.simple_spinner_item,
            mutableListOf()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = adapter

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val type = when (checkedId) {
                R.id.expenseRadio -> EntryType.EXPENSE
                else -> EntryType.INCOME
            }
            categoryViewModel.loadCategoriesByType(type)
        }

        categoryViewModel.categories.observe(this) { categories ->
            adapter.clear()
            adapter.addAll(categories)
            if (categories.isNotEmpty()) {
                spinner.setSelection(0)
            }
        }

        categoryViewModel.loadCategoriesByType(EntryType.EXPENSE)
    }
    private fun setupFilterSpinner() {
        filterCategorySpinner = findViewById(R.id.filterCategorySpinner)


        val filterAdapter = ArrayAdapter<Any>(
            this,
            android.R.layout.simple_spinner_item,
            mutableListOf("Все категории") as List<Any>
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        filterCategorySpinner.adapter = filterAdapter

        sortSpinner = findViewById(R.id.sortSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.sort_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sortSpinner.adapter = adapter
        }
        categoryViewModel.categories.observe(this) { categories ->
            filterAdapter.clear()
            filterAdapter.add("Все категории")
            filterAdapter.addAll(categories)
        }


        filterCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                val categoryId = when {
                    position == 0 -> null
                    selectedItem is Category -> selectedItem.id
                    else -> null
                }
                viewModel.applyFilters(categoryId, sortSpinner.selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.applyFilters(
                    categoryId = if (filterCategorySpinner.selectedItemPosition == 0) null
                    else (filterCategorySpinner.selectedItem as Category).id,
                    sortMode = position
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadNotes()
    }
}


package com.example.sem2androidproject.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sem2androidproject.R
import com.example.sem2androidproject.data.local.Category
import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.data.toDomain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageCategoriesActivity : AppCompatActivity() {
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_categories)
        viewModel.loadCategoriesByType(EntryType.EXPENSE)
        setupRecyclerView()
        setupAddButton()
        findViewById<RadioGroup>(R.id.typeRadioGroup).setOnCheckedChangeListener { _, checkedId ->
            val type = if (checkedId == R.id.expenseRadio) EntryType.EXPENSE else EntryType.INCOME
            viewModel.loadCategoriesByType(type)
        }

    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        val adapter = CategoryAdapter { category ->
            viewModel.deleteCategory(category)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.categories.observe(this) { categories ->
            adapter.submitList(categories.map { it.toDomain() })
        }
    }

    private fun setupAddButton() {
        findViewById<Button>(R.id.btnAddCategory).setOnClickListener {
            val name = findViewById<EditText>(R.id.categoryNameEditText).text.toString()
            val type = if (findViewById<RadioButton>(R.id.expenseRadio).isChecked) {
                EntryType.EXPENSE
            } else {
                EntryType.INCOME
            }

            if (name.isNotBlank()) {
                viewModel.addCategory(Category(name = name, type = type))
                findViewById<EditText>(R.id.categoryNameEditText).text.clear()
            }
        }
    }
}

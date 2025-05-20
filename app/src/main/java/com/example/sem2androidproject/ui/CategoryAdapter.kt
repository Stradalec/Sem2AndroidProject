package com.example.sem2androidproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sem2androidproject.R
import com.example.sem2androidproject.data.local.Category
import com.example.sem2androidproject.data.toEntity
import com.example.sem2androidproject.domain.model.CategoryModel

class CategoryAdapter(
    private val onDeleteClick: (Category) -> Unit
) : ListAdapter<CategoryModel, CategoryAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.nameTextView.text = category.name
        holder.deleteButton.setOnClickListener { onDeleteClick(category.toEntity()) }
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel) = oldItem == newItem
    }
}

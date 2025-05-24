package com.example.sem2androidproject.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.androidplot.xy.BarFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYPlot
import com.example.sem2androidproject.R
import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.data.local.NoteDAO
import com.example.sem2androidproject.ui.note.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class StatisticsActivity : AppCompatActivity() {
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var xyPlot: XYPlot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        xyPlot = findViewById(R.id.xyPlot)

        xyPlot.title.text = "Расходы по категориям"
        xyPlot.domainTitle.text = "Категории"
        xyPlot.rangeTitle.text = "Сумма"
        findViewById<Button>(R.id.btnSelectPeriod).setOnClickListener {
            showDateRangePicker()
        }


        viewModel.categorySums.observe(this) { sums ->
            updatePieChart(sums)
        }
        findViewById<RadioGroup>(R.id.typeRadioGroup).setOnCheckedChangeListener { _, checkedId ->
            val type = if (checkedId == R.id.expenseRadio) EntryType.EXPENSE else EntryType.INCOME
        }

    }

    private fun updatePieChart(sums: List<NoteDAO.CategorySum>) {

        val series = SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Расходы")

        sums.forEach { sum ->
            val categoryName = viewModel.getCategoryNameById(sum.categoryId)
            val amount = sum.total.toDouble()
            series.addLast(series.size().toDouble(), amount)
        }


        val formatter = BarFormatter(Color.BLUE, Color.GREEN)
        val total = sums.sumOf { it.total }
        findViewById<TextView>(R.id.tvTotal).text = "Общая сумма: $total"
        xyPlot.clear()
        xyPlot.addSeries(series, formatter)
        xyPlot.redraw()
    }


    private fun showDateRangePicker() {
        val startDatePicker = DatePickerDialog(
            this, { _, year, month, day ->
                val startDate = Calendar.getInstance().apply { set(year, month, day) }

                val endDatePicker = DatePickerDialog(
                    this, { _, endYear, endMonth, endDay ->
                        val endDate =
                            Calendar.getInstance().apply { set(endYear, endMonth, endDay) }
                        loadData(startDate.time, endDate.time)
                    },
                    startDate.get(Calendar.YEAR),
                    startDate.get(Calendar.MONTH),
                    startDate.get(Calendar.DAY_OF_MONTH)
                )

                endDatePicker.show()
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        startDatePicker.show()
    }

    private fun loadData(start: Date, end: Date) {
        viewModel.loadCategorySums(
            EntryType.EXPENSE,
            start,
            end
        )
    }

}

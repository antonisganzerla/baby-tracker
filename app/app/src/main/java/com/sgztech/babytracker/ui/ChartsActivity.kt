package com.sgztech.babytracker.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.RegisterType
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChartsActivity : BaseActivity() {

    private val lineChart: LineChart by lazy { findViewById(R.id.lineChart) }
    private val viewModel: ChartsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, R.string.toolbar_title_charts)

        viewModel.registers.observe(this) { registers ->
            val entries = registers.filter { it.type == RegisterType.WEIGHT }.mapIndexed { index, register ->
                Entry(
                    index.toFloat(), register.description.filter { it.isDigit() }.toFloat(), getDrawable(R.drawable.ic_balance_24)
                )
            }
            val lineDataSet = LineDataSet(entries, getString(R.string.menu_item_weight))
            lineDataSet.setDrawIcons(false)
            val lineData = LineData(listOf(lineDataSet))
            lineChart.data = lineData
            lineChart.refreshDrawableState()
        }
        viewModel.load()
    }
}
package com.sgztech.babytracker.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.devexpress.dxcharts.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZoneId
import java.util.*

class ChartsActivity : BaseActivity() {

    private val lineChart: LineChart by lazy { findViewById(R.id.lineChart) }
    private val chart: Chart by lazy { findViewById(R.id.chart) }
    private val viewModel: ChartsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, R.string.toolbar_title_charts)

        viewModel.registers.observe(this) { registers ->
            val entries =
                registers.filter { it.type == RegisterType.WEIGHT }.mapIndexed { index, register ->
                    Entry(
                        index.toFloat(),
                        value(register).toFloat(),
                        getDrawable(R.drawable.ic_balance_24),
                    )
                }
            val lineDataSet = LineDataSet(entries, getString(R.string.menu_item_weight))
            lineDataSet.setDrawIcons(false)
            val lineData = LineData(listOf(lineDataSet))
            lineChart.data = lineData


            val weights = registers.filter { it.type == RegisterType.WEIGHT }
            val lineSeries = LineSeries()
            lineSeries.data = object : DateTimeSeriesData {
                override fun getDataCount(): Int = weights.size

                override fun getArgument(position: Int): Date = Date.from(
                    weights[position].localDateTime.atZone(ZoneId.systemDefault()).toInstant()
                )

                override fun getValue(position: Int): Double =
                    value(weights[position])

            }
            val label = BubbleSeriesLabel()
            label.position = BubbleSeriesLabelPosition.OUTSIDE
            //label.textPattern = "{VP}%"
            lineSeries.setLabel(label)

            lineSeries.displayName = "Peso"

            val legend = Legend()
            legend.horizontalPosition = LegendHorizontalPosition.CENTER
            legend.verticalPosition = LegendVerticalPosition.TOP_OUTSIDE
            legend.orientation = LegendOrientation.LEFT_TO_RIGHT
            legend.isVisible = true
            chart.legend = legend

            chart.addSeries(lineSeries)
        }
        viewModel.load()
    }

    private fun value(register: Register): Double =
        register.description.replace("KG", "", true).trim().toDouble()
}
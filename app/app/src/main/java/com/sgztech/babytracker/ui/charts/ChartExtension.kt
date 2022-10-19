package com.sgztech.babytracker.ui.charts

import android.content.Context
import com.github.mikephil.charting.charts.Chart
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.DateTimeFormatter

fun Chart<*>.addCustomMarker(
    registers: List<Register>,
    dateTimeFormatter: DateTimeFormatter,
    context: Context,
) {
    val customMarker = MyMarkerView(
        registers = registers,
        dateTimeFormatter = dateTimeFormatter,
        context = context,
        layoutResource = R.layout.custom_marker_view,
    )
    customMarker.chartView = this
    marker = customMarker
}
package com.sgztech.babytracker.ui.charts

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.textview.MaterialTextView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.DateTimeFormatter

class MyMarkerView(
    private val registers: List<Register>,
    private val dateTimeFormatter: DateTimeFormatter,
    context: Context,
    layoutResource: Int,
) : MarkerView(context, layoutResource) {

    private val tvContent: MaterialTextView by lazy { findViewById(R.id.tvContent) }

    override fun refreshContent(entry: Entry, highlight: Highlight?) {
        val register = registers[entry.x.toInt()]
        val formatDate = dateTimeFormatter.format(register.localDateTime.toLocalDate(), "dd/MM/yyyy")
        tvContent.text = formatDate
        super.refreshContent(entry, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}
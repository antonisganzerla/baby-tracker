package com.sgztech.babytracker.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.sgztech.babytracker.R
import com.sgztech.babytracker.ui.ChartsViewModel
import com.sgztech.babytracker.ui.DateTimeFormatter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaperFragment : Fragment() {

    private val barChartDiaper: BarChart by lazy { requireView().findViewById(R.id.barChartDiaper) }
    private val viewModel: ChartsViewModel by viewModel()
    private val dateTimeFormatter: DateTimeFormatter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        inflater.inflate(R.layout.fragment_diaper, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.diaperRegisters.observe(viewLifecycleOwner) { registers ->
            val entries = registers.groupBy { it.localDateTime.toLocalDate() }.entries.mapIndexed { index, register ->
                BarEntry(
                    index.toFloat(),
                    register.value.size.toFloat(),
                    requireActivity().getDrawable(R.drawable.ic_baby_changing_station_24),
                )
            }
            val barDataSet = BarDataSet(entries, getString(R.string.menu_item_weight))
            barDataSet.setDrawIcons(false)
            val lineData = BarData(listOf(barDataSet))
            barChartDiaper.apply {
                data = lineData
                invalidate()
                addCustomMarker(registers, dateTimeFormatter, requireContext())
            }
        }
        viewModel.loadDiaperRegisters()
    }
}
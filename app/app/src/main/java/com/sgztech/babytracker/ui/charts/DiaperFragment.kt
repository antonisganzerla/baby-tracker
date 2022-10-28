package com.sgztech.babytracker.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.textview.MaterialTextView
import com.natura.android.button.TextButton
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterSubType
import com.sgztech.babytracker.ui.ChartsViewModel
import com.sgztech.babytracker.ui.DateTimeFormatter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaperFragment : Fragment() {

    private val barChartDiaper: BarChart by lazy { requireView().findViewById(R.id.barChartDiaper) }
    private val tvWeek: MaterialTextView by lazy { requireView().findViewById(R.id.tvWeek) }
    private val buttonLeft: TextButton by lazy { requireView().findViewById(R.id.buttonLeft) }
    private val buttonRight: TextButton by lazy { requireView().findViewById(R.id.buttonRight) }
    private val viewModel: ChartsViewModel by viewModel()
    private val dateTimeFormatter: DateTimeFormatter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        inflater.inflate(R.layout.fragment_diaper, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeEvents()
        viewModel.loadDiaperRegisters()
    }

    private fun setupListeners() {
        buttonLeft.setOnClickListener {
            viewModel.minusWeeks()
        }
        buttonRight.setOnClickListener {
            viewModel.plusWeeks()
        }
    }

    private fun observeEvents() {
        viewModel.diaperRegisters.observe(viewLifecycleOwner) { registers ->
            loadBarChart(registers)
        }

        viewModel.date.observe(viewLifecycleOwner) { date ->
            tvWeek.text = viewModel.formatWeekDate(date)
            viewModel.loadDiaperRegisters()
        }
    }

    private fun loadBarChart(registers: List<Register>) {
        val entries =
            registers.groupBy { it.localDateTime.toLocalDate() }.entries.mapIndexed { index, item ->
                BarEntry(
                    index.toFloat(),
                    floatArrayOf(
                        item.value.count { it.subType == RegisterSubType.PEE }.toFloat(),
                        item.value.count { it.subType == RegisterSubType.POO }.toFloat(),
                        item.value.count { it.subType == RegisterSubType.POO_AND_PEE }.toFloat(),
                    ),
                    requireActivity().getDrawable(R.drawable.ic_baby_changing_station_24),
                )
            }
        val barDataSet = BarDataSet(entries, getString(R.string.menu_item_diaper)).apply {
            setDrawIcons(false)
            colors = getMaterialColors()
            stackLabels = resources.getStringArray(R.array.diaper_options)
        }
        val lineData = BarData(listOf(barDataSet))
        barChartDiaper.apply {
            description.isEnabled = false
            data = lineData
            invalidate()
            addCustomMarker(registers, dateTimeFormatter, requireContext())
        }
    }

    private fun getMaterialColors(): List<Int> {
        val colors = IntArray(3)
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3)
        return colors.toList()
    }
}
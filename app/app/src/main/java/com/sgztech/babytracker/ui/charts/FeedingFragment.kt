package com.sgztech.babytracker.ui.charts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sgztech.babytracker.model.RegisterType
import com.sgztech.babytracker.ui.ChartsViewModel
import com.sgztech.babytracker.ui.DateTimeFormatter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedingFragment : Fragment() {

    private val barChartFeeding: BarChart by lazy { requireView().findViewById(R.id.barChartFeeding) }
    private val barChartBabyBottle: BarChart by lazy { requireView().findViewById(R.id.barChartBabyBottle) }
    private val tvWeek: MaterialTextView by lazy { requireView().findViewById(R.id.tvWeek) }
    private val buttonLeft: TextButton by lazy { requireView().findViewById(R.id.buttonLeft) }
    private val buttonRight: TextButton by lazy { requireView().findViewById(R.id.buttonRight) }
    private val viewModel: ChartsViewModel by viewModel()
    private val dateTimeFormatter: DateTimeFormatter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_feeding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeEvents()
        viewModel.loadFeedingRegisters()
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
        viewModel.feedingRegisters.observe(viewLifecycleOwner) { registers ->
            val label = getString(R.string.menu_item_feeding).plus(" (min)")
            barChartFeeding.load(registers, label)
        }
        viewModel.babyBottleRegisters.observe(viewLifecycleOwner) { registers ->
            val label = getString(R.string.menu_item_baby_bottle).plus(" (ml)")
            barChartBabyBottle.load(registers, label)
        }

        viewModel.date.observe(viewLifecycleOwner) { date ->
            tvWeek.text = viewModel.formatWeekDate(date)
            viewModel.loadFeedingRegisters()
        }
    }

    private fun BarChart.load(registers: List<Register>, label: String) {
        val entries =
            registers.groupBy { it.localDateTime.toLocalDate() }.entries.mapIndexed { index, item ->
                BarEntry(
                    index.toFloat(),
                    item.value.map {
                        if (it.type == RegisterType.BREAST_FEEDING)
                            it.description.filter { char -> char.isDigit() }.run {
                                val hours = substring(0, 2)
                                val minutes = substring(2, 4)
                                val seconds = substring(4, 6)
                                val sum = (hours.toInt() * 60) + minutes.toInt() + (seconds.toInt() / 60)
                                sum.toFloat()
                            }
                        else
                            it.description.filter { char -> char.isDigit() }.toFloat()
                    }.toFloatArray(),
                    requireActivity().getDrawable(R.drawable.ic_food_bank_24),
                )
            }
        val barDataSet = BarDataSet(entries, label).apply {
            setDrawIcons(false)
        }
        val lineData = BarData(listOf(barDataSet))
        this.apply {
            description.isEnabled = false
            data = lineData
            invalidate()
            addCustomMarker(registers, dateTimeFormatter, requireContext())
        }
    }
}
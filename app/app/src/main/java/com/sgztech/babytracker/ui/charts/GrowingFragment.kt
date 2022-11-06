package com.sgztech.babytracker.ui.charts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.ChartsViewModel
import com.sgztech.babytracker.ui.DateTimeFormatter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GrowingFragment : Fragment() {

    private val lineChartWeight: LineChart by lazy { requireView().findViewById(R.id.lineChartWeight) }
    private val lineChartHeight: LineChart by lazy { requireView().findViewById(R.id.lineChartHeight) }
    private val viewModel: ChartsViewModel by viewModel()
    private val dateTimeFormatter: DateTimeFormatter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        inflater.inflate(R.layout.fragment_growing, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.weightRegisters.observe(viewLifecycleOwner) { registers ->
            val entries = registers.mapIndexed { index, register ->
                Entry(
                    index.toFloat(),
                    register.extractWeight(),
                    requireActivity().getDrawable(R.drawable.ic_balance_24),
                )
            }
            val lineDataSet = LineDataSet(entries, getString(R.string.menu_item_weight).plus(" (Kg)"))
            lineDataSet.setDrawIcons(false)
            val lineData = LineData(listOf(lineDataSet))
            lineChartWeight.apply {
                description.isEnabled = false
                data = lineData
                invalidate()
                addCustomMarker(registers, dateTimeFormatter, requireContext())
            }
        }

        viewModel.heightRegisters.observe(viewLifecycleOwner) { registers ->
            val entries = registers.mapIndexed { index, register ->
                Entry(
                    index.toFloat(),
                    register.extractHeight(),
                    requireActivity().getDrawable(R.drawable.ic_height_24),
                )
            }
            val lineDataSet = LineDataSet(entries, getString(R.string.menu_item_height).plus(" (cm)"))
            lineDataSet.setDrawIcons(false)
            val lineData = LineData(listOf(lineDataSet))
            lineChartHeight.apply {
                description.isEnabled = false
                data = lineData
                invalidate()
                addCustomMarker(registers, dateTimeFormatter, requireContext())
            }
        }

        viewModel.loadWeightRegisters()
        viewModel.loadHeightRegisters()
    }

    private fun Register.extractWeight(): Float =
        description.replace("KG", "", true).trim().toFloat()

    private fun Register.extractHeight(): Float =
        description.replace("cm", "", true).trim().toFloat()
}

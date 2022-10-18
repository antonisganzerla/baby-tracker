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
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.sgztech.babytracker.R
import com.sgztech.babytracker.extension.showSnackbar
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.ChartsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GrowingFragment : Fragment() {

    private val lineChart: LineChart by lazy { requireView().findViewById(R.id.lineChart) }
    private val viewModel: ChartsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_growing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registers.observe(viewLifecycleOwner) { registers ->
            val entries = registers.mapIndexed { index, register ->
                Entry(
                    index.toFloat(),
                    register.extractWeight(),
                    requireActivity().getDrawable(R.drawable.ic_balance_24),
                )
            }
            val lineDataSet = LineDataSet(entries, getString(R.string.menu_item_weight))
            lineDataSet.setDrawIcons(false)
            val lineData = LineData(listOf(lineDataSet))
            lineChart.data = lineData
            lineData.notifyDataChanged()
            lineChart.notifyDataSetChanged()
        }

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
                entry?.let {
                    val register = viewModel.getRegisterByIndex(entry.x.toInt())
                    register?.let {
                        val formatDate = viewModel.formatDate(register.localDateTime)
                        lineChart.showSnackbar(formatDate)
                    }
                }
            }

            override fun onNothingSelected() {}
        })
        viewModel.load()
    }

    private fun Register.extractWeight(): Float =
        description.replace("KG", "", true).trim().toFloat()
}

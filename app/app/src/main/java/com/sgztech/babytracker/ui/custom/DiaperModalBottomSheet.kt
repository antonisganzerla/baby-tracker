package com.sgztech.babytracker.ui.custom

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TimePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class DiaperModalBottomSheet(
    private val actionButtonClick: (register: Register) -> Unit,
) : BottomSheetDialogFragment() {

    private val textInputTimeSelector: TextInputLayout by lazy {
        requireView().findViewById(
            R.id.textInputTimeSelector
        )
    }
    private val autoCompleteTimeSelector: AutoCompleteTextView by lazy {
        requireView().findViewById(
            R.id.autoCompleteTimeSelector
        )
    }
    private val autoCompleteTypeSelector: AutoCompleteTextView by lazy {
        requireView().findViewById(
            R.id.autoCompleteTypeSelector
        )
    }
    private val btnSave: MaterialButton by lazy { requireView().findViewById(R.id.btnSave) }

    private var hour: Int = LocalTime.now().hour
    private var minute: Int = LocalTime.now().minute

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.diaper_modal_bottom_sheet_content, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textInputTimeSelector.setEndIconOnClickListener {
            popTimePicker()
        }
        autoCompleteTimeSelector.apply {
            setText(formatTime())
            dropDownHeight = 0
            setOnClickListener {
                popTimePicker()
            }
        }
        autoCompleteTypeSelector.setAdapter(buildArrayAdapter(view.resources.getStringArray(R.array.diaper_options)))
        btnSave.setOnClickListener {
            actionButtonClick(Register(
                icon = R.drawable.ic_baby_changing_station_24,
                name = getString(R.string.menu_item_diaper),
                description = autoCompleteTypeSelector.text.toString(),
                time = LocalDateTime.now().withHour(hour).withMinute(minute),
            ))
            dismiss()
        }
    }

    private fun popTimePicker() {
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int ->
                hour = selectedHour
                minute = selectedMinute
                autoCompleteTimeSelector.setText(formatTime())
            }

        TimePickerDialog(requireContext(), onTimeSetListener, hour, minute, true).show()
    }

    private fun formatTime() = String.format(Locale("pt", "BR"), "%02d:%02d", hour, minute)

    private fun buildArrayAdapter(items: Array<String>): ArrayAdapter<String> =
        ArrayAdapter(requireView().context, R.layout.dropdown_item, items)

    companion object {
        const val TAG = "DiaperModalBottomSheet"
    }
}
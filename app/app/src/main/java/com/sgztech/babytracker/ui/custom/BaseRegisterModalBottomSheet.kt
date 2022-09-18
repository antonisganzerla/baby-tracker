package com.sgztech.babytracker.ui.custom

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.annotation.IdRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.util.brazilianLocale
import java.time.LocalTime

abstract class BaseRegisterModalBottomSheet(
    @IdRes
    private val timeSelectorId: Int,
    @IdRes
    private val noteInputId: Int,
) : BottomSheetDialogFragment() {

    private var hour: Int = LocalTime.now().hour
    private var minute: Int = LocalTime.now().minute

    private val timeSelector: TextInputLayout by lazy {
        requireView().findViewById(timeSelectorId)
    }
    private val noteInput: TextInputLayout by lazy {
        requireView().findViewById(noteInputId)
    }

    private val autoCompleteTimeSelector: MaterialAutoCompleteTextView by lazy {
        timeSelector.findViewById(
            R.id.autoCompleteTimeSelector
        )
    }

    private val etNote: TextInputEditText by lazy {
        noteInput.findViewById(R.id.etNote)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeSelector.setEndIconOnClickListener {
            popTimePicker()
        }
        autoCompleteTimeSelector.apply {
            setText(formatTime())
            dropDownHeight = 0
            setOnClickListener {
                popTimePicker()
            }
        }
    }

    private fun popTimePicker() {
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
                hour = selectedHour
                minute = selectedMinute
                autoCompleteTimeSelector.setText(formatTime())
            }

        TimePickerDialog(requireContext(), onTimeSetListener, hour, minute, true).show()
    }

    private fun formatTime() = String.format(brazilianLocale(), "%02d:%02d", hour, minute)

    fun getHour(): Int = hour
    fun getMinute(): Int = minute
    fun getNote(): String = etNote.text.toString()
}
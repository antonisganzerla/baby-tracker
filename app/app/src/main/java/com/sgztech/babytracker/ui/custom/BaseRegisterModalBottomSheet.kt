package com.sgztech.babytracker.ui.custom

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TimePicker
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.*
import com.sgztech.babytracker.extension.*
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.DateTimeFormatter
import java.time.LocalTime

abstract class BaseRegisterModalBottomSheet(
    @IdRes
    private val timeSelectorId: Int,
    @IdRes
    private val noteInputId: Int,
    @IdRes
    private val saveButtonId: Int,
    protected val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter(),
) : BottomSheetDialogFragment() {

    abstract val actionButtonClick: (register: Register, handleResult: (result: Result<Unit, Error>) -> Unit) -> Unit
    abstract val successCallback: () -> Unit

    private var hour: Int = LocalTime.now().hour
    private var minute: Int = LocalTime.now().minute

    private val timeSelector: TextInputLayout by lazy {
        requireView().findViewById(timeSelectorId)
    }
    private val noteInput: TextInputLayout by lazy {
        requireView().findViewById(noteInputId)
    }
    private val btnSave: AppCompatButton by lazy {
        requireView().findViewById(saveButtonId)
    }

    private val rootView: View? by lazy {
        dialog?.window?.decorView?.findViewById(android.R.id.content)
    }

    private val autoCompleteTimeSelector: MaterialAutoCompleteTextView by lazy {
        timeSelector.findViewById(
            R.id.autoCompleteTimeSelector
        )
    }

    private val pbRegister: ProgressBar by lazy {
        requireView().findViewById(R.id.pbRegister)
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
        btnSave.setOnClickListener {
            pbRegister.visible()
            activity?.disableUserInteraction()
            dialog?.window?.disableUserInteraction()
            dialog?.setCancelable(false)
            actionButtonClick(
                buildRegister(),
            ) { result ->
                pbRegister.gone()
                activity?.enableUserInteraction()
                dialog?.window?.enableUserInteraction()
                dialog?.setCancelable(true)
                handleResult(result, successCallback)
            }
        }
    }

    abstract fun buildRegister(): Register

    private fun popTimePicker() {
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
                hour = selectedHour
                minute = selectedMinute
                autoCompleteTimeSelector.setText(formatTime())
            }

        TimePickerDialog(requireContext(), onTimeSetListener, hour, minute, true).show()
    }

    private fun formatTime() = dateTimeFormatter.formatHours(hour, minute)

    fun disableTimeSelector() {
        timeSelector.isEnabled = false
        autoCompleteTimeSelector.isEnabled = false
    }

    fun getHour(): Int = hour
    fun getMinute(): Int = minute
    fun getNote(): String = etNote.text.toString().trim()

    protected fun buildArrayAdapter(items: Array<String>): ArrayAdapter<String> =
        ArrayAdapter(requireView().context, R.layout.dropdown_item, items)

    protected fun updateLayoutParamsTimeSelector(
        block: ConstraintLayout.LayoutParams.() -> Unit,
    ) {
        timeSelector.updateLayoutParams<ConstraintLayout.LayoutParams> {
            block()
        }
    }

    private fun handleResult(result: Result<Unit, Error>, successCallback: () -> Unit) {
        when (result) {
            is Result.Failure -> when (result.error) {
                is Error.NetWork -> rootView?.showSnackbar(result.error.toGenericFailure().errorRes)
                is Error.Unknown -> rootView?.showSnackbar(result.error.toGenericFailure().errorRes)
                is Error.Validation -> rootView?.showSnackbar(result.error.toValidationFailure().errors.joinToString())
                is Error.Auth -> rootView?.showSnackbar(result.error.toAuthFailure().errorRes)
            }
            is Result.Success -> {
                dismiss()
                successCallback()
            }
        }
    }
}

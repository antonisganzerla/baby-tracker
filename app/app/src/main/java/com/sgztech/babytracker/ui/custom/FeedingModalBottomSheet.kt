package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType
import com.sgztech.babytracker.ui.DateTimeFormatter
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class FeedingModalBottomSheet(
    private val date: LocalDate,
    private val actionButtonClick: (register: Register) -> Unit,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter(),
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote) {

    private val textInputTypeSelector: TextInputLayout by lazy {
        requireView().findViewById(R.id.textInputTypeSelector)
    }
    private val autoCompleteTypeSelector: MaterialAutoCompleteTextView by lazy {
        requireView().findViewById(
            R.id.autoCompleteTypeSelector
        )
    }
    private val panelBreastFeeding: LinearLayout by lazy { requireView().findViewById(R.id.panelBreastFeeding) }
    private val panelBabyBottle: LinearLayout by lazy { requireView().findViewById(R.id.panelBabyBottle) }
    private val etQuantity: TextInputEditText by lazy { requireView().findViewById(R.id.etQuantity) }
    private val chronometer: Chronometer by lazy { requireView().findViewById(R.id.chronometer) }
    private val ivToggle: ImageView by lazy { requireView().findViewById(R.id.ivToggle) }
    private val btnSave: AppCompatButton by lazy { requireView().findViewById(R.id.btnSave) }
    private var timeOffset: Long = 0
    private var chronometerIsRunning: Boolean = false
    private var toogleChecked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.feeding_modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAutoCompleteTypeSelector(view)
        btnSave.setOnClickListener {
            actionButtonClick(
                Register(
                    icon = R.drawable.ic_food_bank_24,
                    name = autoCompleteTypeSelector.text.toString(),
                    description = getDescription(),
                    localDateTime = date.atTime(getHour(), getMinute()),
                    note = getNote(),
                    type = getType(),
                )
            )
            dismiss()
        }
        setupToggleButton()
    }

    private fun setupAutoCompleteTypeSelector(view: View) {
        val items = view.resources.getStringArray(R.array.feeding_options)
        autoCompleteTypeSelector.setAdapter(buildArrayAdapter(items))
        autoCompleteTypeSelector.setText(items.first(), false)
        autoCompleteTypeSelector.setOnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                panelBabyBottle.visibility = View.GONE
                panelBreastFeeding.visibility = View.VISIBLE
                updateLayoutParamsTimeSelector(panelBreastFeeding.id)
            } else {
                panelBabyBottle.visibility = View.VISIBLE
                panelBreastFeeding.visibility = View.GONE
                updateLayoutParamsTimeSelector(panelBabyBottle.id)
            }
        }
    }

    private fun updateLayoutParamsTimeSelector(id: Int) {
        updateLayoutParamsTimeSelector {
            endToEnd = id
            startToStart = id
            topToBottom = id
        }
    }

    private fun setupToggleButton() {
        ivToggle.apply {
            setOnClickListener {
                toogleChecked = !toogleChecked
                if (toogleChecked) {
                    if (timeOffset == 0L) {
                        timeOffset = calculateTimeDifference()
                        disableTimeSelector()
                        disableTypeSelector()
                    }
                    startChronometer()
                    setImageResource(R.drawable.ic_pause_circle_24)
                } else {
                    stopChronometer()
                    setImageResource(R.drawable.ic_play_circle_24)
                }
            }
        }
    }

    fun disableTypeSelector() {
        textInputTypeSelector.isEnabled = false
        autoCompleteTypeSelector.isEnabled = false
    }

    private fun startChronometer() {
        chronometer.base = SystemClock.elapsedRealtime() - timeOffset
        chronometer.start()
        chronometerIsRunning = true
    }

    private fun stopChronometer() {
        chronometer.stop()
        timeOffset = SystemClock.elapsedRealtime() - chronometer.base
        chronometerIsRunning = false
    }

    private fun getFormattedTime(): String {
        val localDateTime = dateTimeFormatter.localDateTimeOfMillis(timeOffset)
        return dateTimeFormatter.formatHours(localDateTime.hour,
            localDateTime.minute,
            localDateTime.second)
    }

    private fun calculateTimeDifference(): Long {
        val dateOfTimeSelector = LocalDateTime.now().withHour(getHour()).withMinute(getMinute())
        val currentDate = LocalDateTime.now()
        return Duration.between(dateOfTimeSelector, currentDate).toMillis()
    }

    private fun getDescription(): String =
        if (panelBreastFeeding.isVisible)
            getFormattedTime()
        else
            etQuantity.text.toString().plus(" ".plus("ml"))

    private fun getType(): RegisterType =
        if (panelBreastFeeding.isVisible)
            RegisterType.BREAST_FEEDING
        else
            RegisterType.BABY_BOTTLE

    companion object {
        const val TAG = "FeedingModalBottomSheet"
    }
}
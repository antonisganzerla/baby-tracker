package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.DateTimeFormatter
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class SleepModalBottomSheet(
    private val date: LocalDate,
    private val actionButtonClick: (register: Register) -> Unit,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter(),
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote) {

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
    ): View? = inflater.inflate(R.layout.sleep_modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave.setOnClickListener {
            if (chronometerIsRunning) {
                stopChronometer()
            }
            actionButtonClick(
                Register(
                    icon = R.drawable.ic_bedroom_baby_24,
                    name = getString(R.string.menu_item_nap),
                    description = getFormattedTime(),
                    localDateTime = date.atTime(getHour(), getMinute()),
                    duration = timeOffset,
                    note = getNote(),
                )
            )
            dismiss()
        }
        setupToggleButton()
    }

    private fun setupToggleButton() {
        ivToggle.apply {
            setOnClickListener {
                toogleChecked = !toogleChecked
                if (toogleChecked) {
                    if (timeOffset == 0L) {
                        timeOffset = calculateTimeDifference()
                        disableTimeSelector()
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

    companion object {
        const val TAG = "SleepModalBottomSheet"
    }
}
package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import java.time.LocalDate

class MedicalAppointmentModalBottomSheet(
    private val date: LocalDate,
    private val actionButtonClick: (register: Register) -> Unit,
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote) {

    private val btnSave: AppCompatButton by lazy { requireView().findViewById(R.id.btnSave) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.medical_appointment_modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave.setOnClickListener {
            actionButtonClick(
                Register(
                    icon = R.drawable.ic_local_hospital_24,
                    name = getString(R.string.medical_appointment),
                    description = "",
                    localDateTime = date.atTime(getHour(), getMinute()),
                    note = getNote(),
                )
            )
            dismiss()
        }
    }

    companion object {
        const val TAG = "MedicalAppointmentModalBottomSheet"
    }
}
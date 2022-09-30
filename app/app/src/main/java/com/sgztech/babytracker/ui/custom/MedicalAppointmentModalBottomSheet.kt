package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType
import java.time.LocalDate

class MedicalAppointmentModalBottomSheet(
    private val date: LocalDate,
    override val actionButtonClick: (register: Register, handleResult: (result: Result<Unit, Error>) -> Unit) -> Unit,
    override val successCallback: () -> Unit,
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote, R.id.btnSave) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.medical_appointment_modal_bottom_sheet, container, false)

    override fun buildRegister(): Register =
        Register(
            icon = R.drawable.ic_local_hospital_24,
            name = getString(R.string.medical_appointment),
            description = "",
            localDateTime = date.atTime(getHour(), getMinute()),
            note = getNote(),
            type = RegisterType.MEDICAL,
        )

    companion object {
        const val TAG = "MedicalAppointmentModalBottomSheet"
    }
}
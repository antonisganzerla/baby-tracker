package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType
import java.time.LocalDate

class WeightModalBottomSheet(
    private val date: LocalDate,
    override val actionButtonClick: (register: Register, handleResult: (result: Result<Unit, Error>) -> Unit) -> Unit,
    override val successCallback: () -> Unit,
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote, R.id.btnSave) {

    private val etWeight: TextInputEditText by lazy { requireView().findViewById(R.id.etWeight) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.weight_modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etWeight.addTextChangedListener(MaskWatcher("##.###"))
        etWeight.setText("0.000")
    }

    override fun buildRegister(): Register =
        Register(
            icon = R.drawable.ic_balance_24,
            name = getString(R.string.menu_item_weight),
            description = etWeight.text.toString().plus(" ".plus("kg")),
            localDateTime = date.atTime(getHour(), getMinute()),
            note = getNote(),
            type = RegisterType.WEIGHT,
        )

    companion object {
        const val TAG = "WeightModalBottomSheet"
    }
}
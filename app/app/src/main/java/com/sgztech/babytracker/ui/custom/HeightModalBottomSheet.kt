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

class HeightModalBottomSheet (
    private val date: LocalDate,
    override val actionButtonClick: (register: Register, handleResult: (result: Result<Unit, Error>) -> Unit) -> Unit,
    override val successCallback: () -> Unit,
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote, R.id.btnSave) {

    private val etHeight: TextInputEditText by lazy { requireView().findViewById(R.id.etHeight) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.height_modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etHeight.addTextChangedListener(MaskWatcher("##.#"))
        etHeight.setText("0.0")
    }

    override fun buildRegister(): Register =
        Register(
            icon = R.drawable.ic_height_24,
            name = getString(R.string.menu_item_height),
            description = etHeight.text.toString().plus(" ".plus("cm")),
            localDateTime = date.atTime(getHour(), getMinute()),
            note = getNote(),
            type = RegisterType.HEIGHT,
        )

    companion object {
        const val TAG = "HeightModalBottomSheet"
    }
}
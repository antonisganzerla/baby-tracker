package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import java.time.LocalDate

class HeightModalBottomSheet (
    private val date: LocalDate,
    private val actionButtonClick: (register: Register) -> Unit,
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote) {

    private val etHeight: TextInputEditText by lazy { requireView().findViewById(R.id.etHeight) }
    private val btnSave: AppCompatButton by lazy { requireView().findViewById(R.id.btnSave) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.height_modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etHeight.addTextChangedListener(MaskWatcher("##.#"))
        etHeight.setText("0.0")
        btnSave.setOnClickListener {
            actionButtonClick(
                Register(
                    icon = R.drawable.ic_height_24,
                    name = getString(R.string.menu_item_weight),
                    description = etHeight.text.toString().plus(" ".plus("cm")),
                    localDateTime = date.atTime(getHour(), getMinute()),
                    note = getNote(),
                )
            )
            dismiss()
        }
    }

    companion object {
        const val TAG = "HeightModalBottomSheet"
    }
}
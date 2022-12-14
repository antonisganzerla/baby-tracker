package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.arch.Error
import com.sgztech.babytracker.arch.Result
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterSubType
import com.sgztech.babytracker.model.RegisterType
import java.time.LocalDate

class DiaperModalBottomSheet(
    private val date: LocalDate,
    override val actionButtonClick: (register: Register, handleResult: (result: Result<Unit, Error>) -> Unit) -> Unit,
    override val successCallback: () -> Unit,
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote, R.id.btnSave) {

    private val autoCompleteTypeSelector: MaterialAutoCompleteTextView by lazy {
        requireView().findViewById(
            R.id.autoCompleteTypeSelector
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.diaper_modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = view.resources.getStringArray(R.array.diaper_options)
        autoCompleteTypeSelector.setAdapter(buildArrayAdapter(items))
        autoCompleteTypeSelector.setText(items.first(), false)
    }

    override fun buildRegister(): Register =
        Register(
            icon = R.drawable.ic_baby_changing_station_24,
            name = getString(R.string.menu_item_diaper),
            description = autoCompleteTypeSelector.text.toString(),
            localDateTime = date.atTime(getHour(), getMinute()),
            note = getNote(),
            type = RegisterType.DIAPER,
            subType = getSubType(),
        )

    private fun getSubType(): RegisterSubType? =
        when (autoCompleteTypeSelector.text.toString()) {
            "Xixi" -> RegisterSubType.PEE
            "Coc??" -> RegisterSubType.POO
            "Xixi e coc??" -> RegisterSubType.POO_AND_PEE
            else -> null
        }

    companion object {
        const val TAG = "DiaperModalBottomSheet"
    }
}
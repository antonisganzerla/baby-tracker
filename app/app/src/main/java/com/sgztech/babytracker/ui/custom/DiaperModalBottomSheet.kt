package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import java.time.LocalDateTime

class DiaperModalBottomSheet(
    private val actionButtonClick: (register: Register) -> Unit,
) : BaseRegisterModalBottomSheet(R.id.timeSelector, R.id.textNote) {

    private val autoCompleteTypeSelector: MaterialAutoCompleteTextView by lazy {
        requireView().findViewById(
            R.id.autoCompleteTypeSelector
        )
    }
    private val btnSave: MaterialButton by lazy { requireView().findViewById(R.id.btnSave) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.diaper_modal_bottom_sheet_content, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = buildArrayAdapter(view.resources.getStringArray(R.array.diaper_options))
        autoCompleteTypeSelector.setAdapter(options)
        autoCompleteTypeSelector.setText(options.getItem(0))
        btnSave.setOnClickListener {
            actionButtonClick(Register(
                icon = R.drawable.ic_baby_changing_station_24,
                name = getString(R.string.menu_item_diaper),
                description = autoCompleteTypeSelector.text.toString(),
                time = LocalDateTime.now().withHour(getHour()).withMinute(getMinute()),
                note = getNote(),
            ))
            dismiss()
        }
    }

    private fun buildArrayAdapter(items: Array<String>): ArrayAdapter<String> =
        ArrayAdapter(requireView().context, R.layout.dropdown_item, items)

    companion object {
        const val TAG = "DiaperModalBottomSheet"
    }
}
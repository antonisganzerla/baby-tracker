package com.sgztech.babytracker.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R

class ModalBottomSheet(
    @DrawableRes
    private val imageRes: Int,
    private val imageText: String,
    private val firstSelector: Selector,
    private val secondSelector: Selector? = null,
    private val actionButtonClick : () -> Unit,
) : BottomSheetDialogFragment() {

    private val ivRegister: ImageView by lazy { requireView().findViewById(R.id.ivRegister) }
    private val tvRegister: TextView by lazy { requireView().findViewById(R.id.tvRegister) }
    private val tvFirstSelector: TextView by lazy { requireView().findViewById(R.id.tvFirstSelector) }
    private val tvSecondSelector: TextView by lazy { requireView().findViewById(R.id.tvSecondSelector) }
    private val textInputFirstSelector: TextInputLayout by lazy { requireView().findViewById(R.id.textInputFirstSelector) }
    private val textInputSecondSelector: TextInputLayout by lazy { requireView().findViewById(R.id.textInputSecondSelector) }
    private val autoCompleteFirstSelector: AutoCompleteTextView by lazy { requireView().findViewById(R.id.autoCompleteFirstSelector) }
    private val autoCompleteSecondSelector: AutoCompleteTextView by lazy { requireView().findViewById(R.id.autoCompleteSecondSelector) }
    private val btnSave: MaterialButton by lazy { requireView().findViewById(R.id.btnSave) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivRegister.setImageResource(imageRes)
        tvRegister.text = imageText
        tvFirstSelector.text = firstSelector.text
        textInputFirstSelector.hint = firstSelector.hint
        autoCompleteFirstSelector.setAdapter(buildArrayAdapter(firstSelector.options))

        if (secondSelector == null) {
            tvSecondSelector.visibility = View.GONE
            textInputSecondSelector.visibility = View.GONE
        } else {
            tvSecondSelector.text = secondSelector.text
            textInputSecondSelector.hint = secondSelector.hint
            autoCompleteSecondSelector.setAdapter(buildArrayAdapter(secondSelector.options))
        }
        btnSave.setOnClickListener {
            actionButtonClick()
            dismiss()
        }
    }

    private fun buildArrayAdapter(items: List<String>): ArrayAdapter<String> =
        ArrayAdapter(requireView().context, R.layout.dropdown_item, items)

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
package com.sgztech.babytracker.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.model.Baby
import com.sgztech.babytracker.setBaby
import java.time.LocalDate

class BabyViewModel(
    private val formatter: DateTimeFormatter = DateTimeFormatter(),
) : ViewModel() {

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    fun currentDate(): LocalDate = date.value ?: LocalDate.now()

    fun updateDate(date: LocalDate) {
        _date.postValue(date)
    }

    fun formatDate(date: LocalDate): String =
        formatter.format(date, "dd/MM/yyyy")

    fun saveBaby(baby: Baby, context: Context) {
        context.setBaby(baby)
    }
}
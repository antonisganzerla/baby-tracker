package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Baby
import java.time.LocalDate

class BabyViewModel(
    private val preferenceService: PreferenceService,
    private val formatter: DateTimeFormatter,
) : ViewModel() {

    private val _formState: MutableLiveData<BabyFormState> = MutableLiveData()
    val formState: LiveData<BabyFormState> = _formState

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    init {
        _date.postValue(LocalDate.now())
    }

    fun currentDate(): LocalDate = date.value ?: LocalDate.now()

    fun updateDate(date: LocalDate) {
        _date.postValue(date)
    }

    fun formatDate(date: LocalDate): String =
        formatter.format(date, "dd/MM/yyyy")

    fun saveBaby(baby: Baby) {
        preferenceService.setBaby(baby)
    }

    fun getBaby(): Baby =
        preferenceService.getBaby()

    fun validate(name: String, sex: String) {
        if (name.isEmpty()) {
            _formState.postValue(BabyFormState.InvalidName(R.string.msg_enter_name))
            return
        }

        if (sex.isEmpty()) {
            _formState.postValue(BabyFormState.InvalidSex(R.string.msg_enter_sex))
            return
        }

        _formState.postValue(BabyFormState.Valid)
    }
}

sealed class BabyFormState {
    class InvalidName(val errorRes: Int) : BabyFormState()
    class InvalidSex(val errorRes: Int) : BabyFormState()
    object Valid : BabyFormState()
}
package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.R
import com.sgztech.babytracker.dao.BabyDao
import com.sgztech.babytracker.model.Baby
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.User
import kotlinx.coroutines.launch
import java.time.LocalDate

class BabyViewModel(
    private val preferenceService: PreferenceService,
    private val babyDao: BabyDao,
    private val formatter: DateTimeFormatter,
) : ViewModel() {

    private val _formState: MutableLiveData<BabyFormState> = MutableLiveData()
    val formState: LiveData<BabyFormState> = _formState

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    private val user: User = preferenceService.getUser()

    private val _baby: MutableLiveData<Baby?> = MutableLiveData()
    val baby: LiveData<Baby?> = _baby

    init {
        getBaby()
    }

    fun currentDate(): LocalDate = date.value ?: LocalDate.now()

    fun updateDate(date: LocalDate) {
        _date.postValue(date)
    }

    fun formatDate(date: LocalDate): String =
        formatter.format(date, "dd/MM/yyyy")

    fun saveBaby(baby: Baby) {
        viewModelScope.launch {
            babyDao.insertAll(baby.copy(userId = user.id))
        }
    }

    fun update(baby: Baby) {
        viewModelScope.launch {
            babyDao.update(baby.copy(id = _baby.value!!.id, userId = user.id))
        }
    }

    private fun getBaby() =
        viewModelScope.launch {
            val baby = babyDao.loadByUserId(user.id)
            _baby.postValue(baby)
            _date.postValue(baby?.birthday ?: LocalDate.now())
        }

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
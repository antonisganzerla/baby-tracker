package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class ChartsViewModel(
    private val preferenceService: PreferenceService,
    private val dateFormatter: DateTimeFormatter,
    private val repository: RegisterRepository,
) : ViewModel() {

    private val _registers: MutableLiveData<List<Register>> = MutableLiveData()
    val registers: LiveData<List<Register>> = _registers

    fun load() {
        viewModelScope.launch {
            val registers = repository.loadLocal(preferenceService.getUser().id).sortedBy { it.localDateTime }
            _registers.postValue(registers.filter { it.type == RegisterType.WEIGHT })
        }
    }

    fun getRegisterByIndex(index: Int): Register? =
        registers.value?.get(index)


    fun formatDate(date: LocalDateTime): String =
        dateFormatter.format(date.toLocalDate(), "dd/MM/yyyy")
}
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

class ChartsViewModel(
    private val preferenceService: PreferenceService,
    private val repository: RegisterRepository,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel() {

    private val _weightRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val weightRegisters: LiveData<List<Register>> = _weightRegisters

    private val _heightRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val heightRegisters: LiveData<List<Register>> = _heightRegisters

    private val _diaperRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val diaperRegisters: LiveData<List<Register>> = _diaperRegisters

    private var _date: MutableLiveData<LocalDate> = MutableLiveData()
    val date: LiveData<LocalDate> = _date

    init {
        _date.postValue(LocalDate.now().getFirstDayOfWeek())
    }

    fun loadWeightRegisters() {
        _weightRegisters.loadRegistersByType(RegisterType.WEIGHT)
    }

    fun loadHeightRegisters() {
        _heightRegisters.loadRegistersByType(RegisterType.HEIGHT)
    }

    fun loadDiaperRegisters() {
        _diaperRegisters.loadRegistersByType(RegisterType.DIAPER) { register ->
            date.value?.let { startWeek ->
                val endWeek = startWeek.getLastDayOfWeek()
                val registerDate = register.localDateTime.toLocalDate()
                registerDate >= startWeek && registerDate <= endWeek
            } ?: true
        }
    }

    private fun MutableLiveData<List<Register>>.loadRegistersByType(
        type: RegisterType,
        predicateFilter: ((Register) -> Boolean)? = null,
    ) {
        viewModelScope.launch {
            val registers = repository.loadAllByUserIdAndType(
                userId = preferenceService.getUser().id,
                type = type,
            ).sortedBy { it.localDateTime }

            val filteredRegisters = predicateFilter?.let { registers.filter(it) }?.toList() ?: registers

            this@loadRegistersByType.postValue(filteredRegisters)
        }
    }

    fun formatWeekDate(date: LocalDate): String {
        val startWeek = dateTimeFormatter.format(date, DAY_MONTH_PATTERN)
        val endWeek = dateTimeFormatter.format(date.getLastDayOfWeek(), DAY_MONTH_PATTERN)
        return startWeek.plus(" - ").plus(endWeek)
    }

    private fun LocalDate.getFirstDayOfWeek(): LocalDate =
        with(dateTimeFormatter.temporalField(), 1)

    private fun LocalDate.getLastDayOfWeek(): LocalDate =
        with(dateTimeFormatter.temporalField(), 7)

    fun plusWeeks() {
        _date.postValue(date.value?.plusWeeks(1))
        loadDiaperRegisters()
    }

    fun minusWeeks() {
        _date.postValue(date.value?.minusWeeks(1))
        loadDiaperRegisters()
    }
}
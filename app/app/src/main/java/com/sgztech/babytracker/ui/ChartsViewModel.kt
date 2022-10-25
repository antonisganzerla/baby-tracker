package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.R
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.model.RegisterType
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

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
        _date.value = LocalDate.now().getFirstDayOfWeek()
    }

    fun loadWeightRegisters() {
        viewModelScope.launch {
            _weightRegisters.postValue(loadRegisterByType(RegisterType.WEIGHT))
        }
    }

    fun loadHeightRegisters() {
        viewModelScope.launch {
            _heightRegisters.postValue(loadRegisterByType(RegisterType.HEIGHT))
        }
    }

    fun loadDiaperRegisters() {
        viewModelScope.launch {
            val startWeekDay = date.value
            val lastDayOfWeek = startWeekDay?.getLastDayOfWeek()
            val registers = loadRegisterByType(RegisterType.DIAPER).filter { register ->
                startWeekDay?.let { startWeek ->
                    val registerDate = register.localDateTime.toLocalDate()
                    registerDate >= startWeek && registerDate <= lastDayOfWeek
                } ?: true
            }

            val newRegisters = startWeekDay?.let {
                val daysBetween = ChronoUnit.DAYS.between(startWeekDay, lastDayOfWeek)
                val newList = registers.toMutableList()
                for (index in 0..daysBetween) {
                    val date = startWeekDay.plusDays(index)
                    if (registers.any { it.localDateTime.dayOfWeek == date?.dayOfWeek }.not())
                        newList.add(
                            index = index.toInt(),
                            element = Register(
                                icon = R.drawable.ic_baby_changing_station_24,
                                name = "",
                                description = "",
                                localDateTime = date.atStartOfDay(),
                                type = RegisterType.DIAPER,
                            ),
                        )
                }
                newList
            } ?: registers

            _diaperRegisters.postValue(newRegisters)
        }
    }

    private suspend fun loadRegisterByType(type: RegisterType): List<Register> {
        return repository.loadAllByUserIdAndType(
            userId = preferenceService.getUser().id,
            type = type,
        ).sortedBy { it.localDateTime }
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
        _date.value = date.value?.plusWeeks(1)
        loadDiaperRegisters()
    }

    fun minusWeeks() {
        _date.value = date.value?.minusWeeks(1)
        loadDiaperRegisters()
    }
}
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

    private val _feedingRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val feedingRegisters: LiveData<List<Register>> = _feedingRegisters

    private val _babyBottleRegisters: MutableLiveData<List<Register>> = MutableLiveData()
    val babyBottleRegisters: LiveData<List<Register>> = _babyBottleRegisters

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
                register.filterBetween(startWeekDay, lastDayOfWeek)
            }

            val weekRegisters = includeAllDaysOfWeek(startWeekDay, lastDayOfWeek, registers, RegisterType.DIAPER)
            _diaperRegisters.postValue(weekRegisters)
        }
    }

    fun loadFeedingRegisters() {
        viewModelScope.launch {
            val startWeekDay = date.value
            val lastDayOfWeek = startWeekDay?.getLastDayOfWeek()
            val registers = loadRegisterByType(RegisterType.BREAST_FEEDING, RegisterType.BABY_BOTTLE).filter { register ->
                register.filterBetween(startWeekDay, lastDayOfWeek)
            }

            registers.filter { it.type == RegisterType.BREAST_FEEDING }.apply {
                val weekRegisters = includeAllDaysOfWeek(startWeekDay, lastDayOfWeek, this, RegisterType.BREAST_FEEDING)
                _feedingRegisters.postValue(weekRegisters)
            }

            registers.filter { it.type == RegisterType.BABY_BOTTLE }.apply {
                val weekRegisters = includeAllDaysOfWeek(startWeekDay, lastDayOfWeek, this, RegisterType.BABY_BOTTLE)
                _babyBottleRegisters.postValue(weekRegisters)
            }
        }
    }

    private fun Register.filterBetween(
        startWeekDay: LocalDate?,
        lastDayOfWeek: LocalDate?,
    ): Boolean =
        startWeekDay?.let { startWeek ->
            val registerDate = localDateTime.toLocalDate()
            registerDate >= startWeek && registerDate <= lastDayOfWeek
        } ?: true

    private fun includeAllDaysOfWeek(
        startWeekDay: LocalDate?,
        lastDayOfWeek: LocalDate?,
        registers: List<Register>,
        type: RegisterType,
    ): List<Register> {
        val registersWithAllDaysOfWeek = startWeekDay?.let {
            val daysBetween = ChronoUnit.DAYS.between(startWeekDay, lastDayOfWeek)
            val copyRegisters = registers.toMutableList()
            for (index in 0..daysBetween) {
                val date = startWeekDay.plusDays(index)
                if (registers.any { it.localDateTime.dayOfWeek == date?.dayOfWeek }.not())
                    copyRegisters.add(
                        index = index.toInt(),
                        element = Register(
                            icon = R.drawable.ic_bar_chart_24,
                            name = "",
                            description = "00:00:00",
                            localDateTime = date.atStartOfDay(),
                            type = type,
                        ),
                    )
            }
            copyRegisters
        } ?: registers
        return registersWithAllDaysOfWeek
    }

    private suspend fun loadRegisterByType(vararg type: RegisterType): List<Register> {
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
    }

    fun minusWeeks() {
        _date.value = date.value?.minusWeeks(1)
    }
}
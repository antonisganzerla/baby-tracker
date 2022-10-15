package com.sgztech.babytracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.model.Register
import kotlinx.coroutines.launch

class ChartsViewModel(
    private val preferenceService: PreferenceService,
    private val repository: RegisterRepository,
) : ViewModel() {

    private val _registers: MutableLiveData<List<Register>> = MutableLiveData()
    val registers: LiveData<List<Register>> = _registers

    fun load() {
        viewModelScope.launch {
            val registers = repository.loadLocal(preferenceService.getUser().id).sortedBy { it.localDateTime }
            _registers.postValue(registers)
        }
    }

}
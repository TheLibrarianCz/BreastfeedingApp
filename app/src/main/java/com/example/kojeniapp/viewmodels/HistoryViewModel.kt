package com.example.kojeniapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kojeniapp.AppDataStore
import com.example.kojeniapp.database.Repository
import com.example.kojeniapp.database.models.DayStatisticsDataModel
import com.example.kojeniapp.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    repository: Repository,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _dayStatistics: MutableStateFlow<List<DayStatisticsDataModel>> =
        MutableStateFlow(emptyList())
    val dayStatistics: StateFlow<List<DayStatisticsDataModel>> = _dayStatistics.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appDataStore.settings.collect {
                _dayStatistics.value =
                    repository.getStatisticsByQuery(it.historyLength).map { model ->
                        DayStatisticsDataModel(
                            date = LocalDate.parse(model.date).format(DateUtils.getDateFormatter()),
                            feedingCount = model.total + 1,
                            averageTimeBetweenFeedings = convertFloat(model.average),
                            maxTimeBetweenFeedings = convertFloat(model.maximum)
                        )
                    }
            }
        }
    }

    private fun convertFloat(value: Float): LocalTime {
        val hour = value.toInt()
        val minute = (value - hour) * 60
        return LocalTime.of(hour, minute.toInt(), 0)
    }
}

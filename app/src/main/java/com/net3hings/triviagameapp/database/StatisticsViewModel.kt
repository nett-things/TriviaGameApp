package com.net3hings.triviagameapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StatisticsViewModel(private val statisticsRepository: StatisticsRepository) : ViewModel() {
	val allItems: LiveData<List<StatisticsItem>> = statisticsRepository.allItems.asLiveData()

	fun addItem(itemQuestions: Int, itemCategory: Int, itemDifficulty: Int, itemType: Int, itemCorrectAnswers: Int, itemScore: Int, itemDuration: Long) = viewModelScope.launch {
		statisticsRepository.insert(
			StatisticsItem(
				questions = itemQuestions,
				category = itemCategory,
				difficulty = itemDifficulty,
				type = itemType,
				correctAnswers = itemCorrectAnswers,
				score = itemScore,
				duration = itemDuration
			)
		)
	}

	fun clearAll() = viewModelScope.launch {
		statisticsRepository.clearAll()
	}

	class StatisticsViewModelFactory(private val statisticsRepository: StatisticsRepository) : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			if(modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
				@Suppress("UNCHECKED_CAST")
				return StatisticsViewModel(statisticsRepository) as T
			}
			throw IllegalArgumentException("Unknown ViewModel class")
		}
	}
}
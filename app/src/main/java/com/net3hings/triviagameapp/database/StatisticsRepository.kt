package com.net3hings.triviagameapp.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class StatisticsRepository(private val statisticsDAO: StatisticsDAO) {
	val allItems: Flow<List<StatisticsItem>> = statisticsDAO.getItems()

	@WorkerThread
	suspend fun insert(item: StatisticsItem) {
		statisticsDAO.insert(item)
	}

	@WorkerThread
	suspend fun clearAll() {
		statisticsDAO.clearAll()
	}
}
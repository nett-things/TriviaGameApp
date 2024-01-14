package com.net3hings.triviagameapp

import android.app.Application
import com.net3hings.triviagameapp.database.StatisticsDatabase
import com.net3hings.triviagameapp.database.StatisticsRepository

class TriviaGameApplication : Application() {
	val database: StatisticsDatabase by lazy { StatisticsDatabase.getDatabase(this) }
	val repository by lazy { StatisticsRepository(database.statisticsDAO()) }
}
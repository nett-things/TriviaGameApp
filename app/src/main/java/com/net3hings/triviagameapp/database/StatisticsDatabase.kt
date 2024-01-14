package com.net3hings.triviagameapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StatisticsItem::class], version = 1, exportSchema = false)
abstract class StatisticsDatabase : RoomDatabase() {
	abstract fun statisticsDAO(): StatisticsDAO

	companion object {
		@Volatile
		private var INSTANCE: StatisticsDatabase? = null

		fun getDatabase(context: Context): StatisticsDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					StatisticsDatabase::class.java,
					"statistics_database"
				).fallbackToDestructiveMigration().build()

				INSTANCE = instance
				return instance
			}
		}
	}
}
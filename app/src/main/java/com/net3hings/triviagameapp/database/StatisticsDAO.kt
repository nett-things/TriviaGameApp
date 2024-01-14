package com.net3hings.triviagameapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDAO {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(item: StatisticsItem)

	@Query("SELECT * FROM statistics")
	fun getItems(): Flow<List<StatisticsItem>>

	@Query("DELETE FROM statistics")
	fun clearAll()
}
package com.net3hings.triviagameapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistics")
data class StatisticsItem(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,

	@ColumnInfo(name = "questions")
	val questions: Int,

	@ColumnInfo(name = "category")
	val category: Int,

	@ColumnInfo(name = "difficulty")
	val difficulty: Int,

	@ColumnInfo(name = "type")
	val type: Int,

	@ColumnInfo(name = "correct_answers")
	val correctAnswers: Int,

	@ColumnInfo(name = "score")
	val score: Int,

	@ColumnInfo(name = "duration")
	val duration: Long
)

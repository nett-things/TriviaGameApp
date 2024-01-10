package com.net3hings.triviagameapp.question

import android.os.Parcelable
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Question(
	@QuestionType
	@Json(name = "type")
	val type: Type,

	@QuestionDifficulty
	@Json(name = "difficulty")
	val difficulty: Difficulty,

	@Json(name = "category")
	val category: String,

	@Json(name = "question")
	val question: String,

	@Json(name = "correct_answer")
	val correctAnswer: String,

	@Json(name = "incorrect_answers")
	val incorrectAnswers: List<String>
): Parcelable

enum class Type {
	MULTIPLE, BOOLEAN
}

enum class Difficulty {
	EASY, MEDIUM, HARD
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class QuestionType

/** Converts question type strings to a question type enum.  */
class QuestionTypeAdapter {
	@ToJson
	fun toJson(@QuestionType type: Type): String {
		return when(type) {
			Type.MULTIPLE -> "multiple"
			Type.BOOLEAN -> "boolean"
		}
	}

	@FromJson
	@QuestionType fun fromJson(type: String): Type {
		return when(type) {
			"multiple" -> Type.MULTIPLE
			"boolean" -> Type.BOOLEAN
			else -> Type.MULTIPLE
		}
	}
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class QuestionDifficulty

/** Converts question difficulty strings to a difficulty type enum.  */
class QuestionDifficultyAdapter {
	@ToJson
	fun toJson(@QuestionDifficulty difficulty: Difficulty): String {
		return when(difficulty) {
			Difficulty.EASY -> "easy"
			Difficulty.MEDIUM -> "medium"
			Difficulty.HARD -> "hard"
		}
	}

	@FromJson
	@QuestionDifficulty fun fromJson(difficulty: String): Difficulty {
		return when(difficulty) {
			"easy" -> Difficulty.EASY
			"medium" -> Difficulty.MEDIUM
			"hard" -> Difficulty.HARD
			else -> Difficulty.EASY
		}
	}
}
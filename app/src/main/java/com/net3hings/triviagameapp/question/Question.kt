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
): Parcelable {
	enum class Type {
		MULTIPLE, BOOLEAN
	}

	enum class Difficulty {
		EASY, MEDIUM, HARD
	}
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class QuestionType

/** Converts question type strings to a question type enum.  */
class QuestionTypeAdapter {
	@ToJson
	fun toJson(@QuestionType type: Question.Type): String {
		return when(type) {
			Question.Type.MULTIPLE -> "multiple"
			Question.Type.BOOLEAN -> "boolean"
		}
	}

	@FromJson
	@QuestionType fun fromJson(type: String): Question.Type {
		return when(type) {
			"multiple" -> Question.Type.MULTIPLE
			"boolean" -> Question.Type.BOOLEAN
			else -> Question.Type.MULTIPLE
		}
	}
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class QuestionDifficulty

/** Converts question difficulty strings to a difficulty type enum.  */
class QuestionDifficultyAdapter {
	@ToJson
	fun toJson(@QuestionDifficulty difficulty: Question.Difficulty): String {
		return when(difficulty) {
			Question.Difficulty.EASY -> "easy"
			Question.Difficulty.MEDIUM -> "medium"
			Question.Difficulty.HARD -> "hard"
		}
	}

	@FromJson
	@QuestionDifficulty fun fromJson(difficulty: String): Question.Difficulty {
		return when(difficulty) {
			"easy" -> Question.Difficulty.EASY
			"medium" -> Question.Difficulty.MEDIUM
			"hard" -> Question.Difficulty.HARD
			else -> Question.Difficulty.EASY
		}
	}
}
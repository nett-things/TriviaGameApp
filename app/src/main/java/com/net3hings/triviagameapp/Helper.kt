package com.net3hings.triviagameapp

import android.content.Context
import android.util.TypedValue
import com.net3hings.triviagameapp.question.Question

object Helper {
	fun resolveCategoryToInt(category: String): Int {
		return when(category) {
			"Any Category" -> 0
			"General Knowledge" -> 9
			"Entertainment: Books" -> 10
			"Entertainment: Film" -> 11
			"Entertainment: Music" -> 12
			"Entertainment: Musicals &amp; Theatres" -> 13
			"Entertainment: Television" -> 14
			"Entertainment: Video Games" -> 15
			"Entertainment: Board Games" -> 16
			"Science &amp; Nature" -> 17
			"Science: Computers" -> 18
			"Science: Mathematics" -> 19
			"Mythology" -> 20
			"Sports" -> 21
			"Geography" -> 22
			"History" -> 23
			"Politics" -> 24
			"Art" -> 25
			"Celebrities" -> 26
			"Animals" -> 27
			"Vehicles" -> 28
			"Entertainment: Comics" -> 29
			"Science: Gadgets" -> 30
			"Entertainment: Japanese Anime &amp; Manga" -> 31
			"Entertainment: Cartoon &amp; Animations" -> 32
			else -> 0
		}
	}

	fun resolveCategoryToString(category: Int): String {
		return when(category) {
			0 -> "Any Category"
			9 -> "General Knowledge"
			10 -> "Entertainment: Books"
			11 -> "Entertainment: Film"
			12 -> "Entertainment: Music"
			13 -> "Entertainment: Musicals &amp; Theatres"
			14 -> "Entertainment: Television"
			15 -> "Entertainment: Video Games"
			16 -> "Entertainment: Board Games"
			17 -> "Science &amp; Nature"
			18 -> "Science: Computers"
			19 -> "Science: Mathematics"
			20 -> "Mythology"
			21 -> "Sports"
			22 -> "Geography"
			23 -> "History"
			24 -> "Politics"
			25 -> "Art"
			26 -> "Celebrities"
			27 -> "Animals"
			28 -> "Vehicles"
			29 -> "Entertainment: Comics"
			30 -> "Science: Gadgets"
			31 -> "Entertainment: Japanese Anime &amp; Manga"
			32 -> "Entertainment: Cartoon &amp; Animations"
			else -> "Unknown"
		}
	}

	fun resolveDifficulty(difficulty: Question.Difficulty): String {
		return when(difficulty) {
			Question.Difficulty.ANY -> "0"
			Question.Difficulty.EASY -> "easy"
			Question.Difficulty.MEDIUM -> "medium"
			Question.Difficulty.HARD -> "hard"
		}
	}

	fun resolveType(type: Question.Type): String {
		return when(type) {
			Question.Type.ANY -> "0"
			Question.Type.MULTIPLE -> "multiple"
			Question.Type.BOOLEAN -> "boolean"
		}
	}

	fun getAttributeValue(context: Context, attr: Int): Int? {
		return TypedValue()
			.apply { context.theme.resolveAttribute(attr, this, true) }
			.resourceId
			.takeUnless { it == 0 }
	}
}
package com.net3hings.triviagameapp

import com.net3hings.triviagameapp.question.Question

object Helper {
	fun resolveCategory(category: Int): String {
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
}
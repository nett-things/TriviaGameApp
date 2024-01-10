package com.net3hings.triviagameapp.question

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Questions(
	@Json(name = "results")
	val items: List<Question>
)
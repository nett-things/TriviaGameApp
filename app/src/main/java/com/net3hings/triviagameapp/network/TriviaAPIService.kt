package com.net3hings.triviagameapp.network

import com.net3hings.triviagameapp.question.QuestionDifficultyAdapter
import com.net3hings.triviagameapp.question.QuestionTypeAdapter
import com.net3hings.triviagameapp.question.Questions
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://opentdb.com/"
private const val QUESTIONS_ENDPOINT = "api.php?amount=50"
private const val QUERY_ENDPOINT = "api.php"

private val moshi = Moshi.Builder()
	.add(QuestionTypeAdapter())
	.add(QuestionDifficultyAdapter())
	.add(KotlinJsonAdapterFactory())
	.build()

private  val retrofit = Retrofit.Builder()
	.addConverterFactory(MoshiConverterFactory.create(moshi))
	.baseUrl(BASE_URL)
	.build()

interface TriviaAPIService {
	@GET(QUESTIONS_ENDPOINT)
	suspend fun getQuestions(): Questions

	@GET(QUERY_ENDPOINT)
	suspend fun getQuestions(
		@Query("amount")
		amount: String,

		@Query("category")
		category: String,

		@Query("difficulty")
		difficulty: String,

		@Query("type")
		type: String
	): Questions
}

object TriviaAPI {
	val retrofitService: TriviaAPIService by lazy { retrofit.create(TriviaAPIService::class.java) }
}
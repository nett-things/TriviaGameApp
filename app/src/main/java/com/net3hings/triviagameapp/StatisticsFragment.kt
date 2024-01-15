package com.net3hings.triviagameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.net3hings.triviagameapp.database.StatisticsItem
import com.net3hings.triviagameapp.database.StatisticsViewModel
import com.net3hings.triviagameapp.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {
	private lateinit var binding: FragmentStatisticsBinding

	private val viewModel: StatisticsViewModel by activityViewModels {
		StatisticsViewModel.StatisticsViewModelFactory(
			(activity?.application as TriviaGameApplication).repository
		)
	}

	private var items: List<StatisticsItem>? = null
	private var mostPlayedCategory: Int = 0
	private var avgCorrectAnswers: Double = 0.0
	private var avgScore: Double = 0.0
	private var avgAnswerTime: Double = 0.0

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentStatisticsBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// set up the listener for handling the go back button
		requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				findNavController().popBackStack(R.id.menuFragment, false)
			}
		})

		viewModel.allItems.observe(viewLifecycleOwner) { itemList ->
			itemList?.let {
				items = itemList

				calculateData()
				displayData()
			}
		}
	}

	private fun calculateData() {
		if(items != null) {
			val categories = items?.map { it.category }
			mostPlayedCategory = categories?.groupingBy { it }?.eachCount()?.maxBy { it.value }?.key!!

			avgCorrectAnswers = items?.sumOf { it.correctAnswers }!!.toDouble() / items?.sumOf { it.questions }!!

			avgScore = items?.map { it.score }!!.average()

			avgAnswerTime = items?.sumOf { it.duration }!!.toDouble() / items?.sumOf { it.questions }!!
		}
	}

	private fun displayData() {
		if(items != null) {
			binding.mostPlayedCategoryLabel.text = getString(
				R.string.most_played_category_label_text,
				resolveCategory(mostPlayedCategory)
			)
			binding.averageCorrectAnswersLabel.text = getString(R.string.average_correct_answers_label_text, avgCorrectAnswers)
			binding.averageScoreLabel.text = getString(R.string.average_score_label_text, avgScore)
			binding.averageAnswerTimeLabel.text = getString(R.string.average_answer_time_label_text, avgAnswerTime)

		} else {
			binding.mostPlayedCategoryLabel.visibility = View.INVISIBLE
			binding.averageCorrectAnswersLabel.visibility = View.INVISIBLE
			binding.averageScoreLabel.visibility = View.INVISIBLE
			binding.averageAnswerTimeLabel.visibility = View.INVISIBLE

			binding.noDataLabel.visibility = View.VISIBLE
		}
	}

	private fun resolveCategory(category: Int): String {
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
}
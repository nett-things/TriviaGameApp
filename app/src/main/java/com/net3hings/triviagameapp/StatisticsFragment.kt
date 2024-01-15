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
		val categories = items?.map { it.category }
		mostPlayedCategory = categories?.groupingBy { it }?.eachCount()?.maxBy { it.value }?.key!!

		avgCorrectAnswers = items?.sumOf { it.correctAnswers }!!.toDouble()/items?.sumOf { it.questions }!!

		avgScore = items?.map { it.score }!!.average()

		avgAnswerTime = items?.sumOf { it.duration }!!.toDouble()/items?.sumOf { it.questions }!!
	}

	private fun displayData() {
		binding.mostPlayedCategoryLabel.text = getString(R.string.most_played_category_label_text, mostPlayedCategory.toString())
		binding.averageCorrectAnswersLabel.text = getString(R.string.average_correct_answers_label_text, avgCorrectAnswers)
		binding.averageScoreLabel.text = getString(R.string.average_score_label_text, avgScore)
		binding.averageAnswerTimeLabel.text = getString(R.string.average_answer_time_label_text, avgAnswerTime)
	}
}
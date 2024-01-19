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
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf

class StatisticsFragment : Fragment() {
	private lateinit var binding: FragmentStatisticsBinding

	private val viewModel: StatisticsViewModel by activityViewModels {
		StatisticsViewModel.StatisticsViewModelFactory(
			(activity?.application as TriviaGameApplication).repository
		)
	}

	private var items: List<StatisticsItem>? = null
	private var mostPlayedCategory: Int = 0
	private var avgCorrectAnswers: MutableList<Double> = mutableListOf()
	private var avgScores: MutableList<Double> = mutableListOf()
	private var avgAnswerTimes: MutableList<Double> = mutableListOf()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentStatisticsBinding.inflate(inflater, container, false)

		(activity as MainActivity).showActionBar()

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
		if(items != null && items!!.isNotEmpty()) {
			mostPlayedCategory = items?.map { it.category }?.groupingBy { it }?.eachCount()?.maxBy { it.value }?.key!!

			calculateAvgCorrectAnswers()
			calculateAvgScores()
			calculateAvgAnswerTimes()
		}
	}

	private fun calculateAvgCorrectAnswers() {
		val ratiosOfCorrectAnswers = items?.map { it.correctAnswers }!!.zip(items?.map { it.questions }!!) { a, b -> a * 100.0 / b }

		for(i in items!!.indices)
			avgCorrectAnswers.add(ratiosOfCorrectAnswers.slice(0..i).average())
	}

	private fun calculateAvgScores() {
		for(i in items!!.indices)
			avgScores.add(items?.map { it.score }!!.slice(0..i).average())
	}

	private fun calculateAvgAnswerTimes() {
		val answerTimes = items?.map { it.duration }!!.zip(items?.map { it.questions }!!) { a, b -> (a / 1000.0) / b }

		for(i in items!!.indices)
			avgAnswerTimes.add(answerTimes.slice(0..i).average())
	}

	private fun displayData() {
		if(items != null && items!!.isNotEmpty()) {
			binding.mostPlayedCategoryLabel.text = getString(
				R.string.most_played_category_label_text,
				Helper.resolveCategory(mostPlayedCategory)
			)

			binding.averageCorrectAnswersLabel.text = getString(R.string.average_correct_answers_label_text, avgCorrectAnswers.last())
			binding.averageCorrectAnswersChart.setModel(entryModelOf(Helper.convertToListOfFloatEntries(avgCorrectAnswers)))

			binding.averageScoreLabel.text = getString(R.string.average_score_label_text, avgScores.last())
			binding.averageScoreChart.setModel(entryModelOf(Helper.convertToListOfFloatEntries(avgScores)))

			binding.averageAnswerTimeLabel.text = getString(R.string.average_answer_time_label_text, avgAnswerTimes.last())
			binding.averageAnswerTimeChart.setModel(entryModelOf(Helper.convertToListOfFloatEntries(avgAnswerTimes)))


		} else {
			binding.mostPlayedCategoryLabel.visibility = View.INVISIBLE
			binding.averageCorrectAnswersLabel.visibility = View.INVISIBLE
			binding.averageScoreLabel.visibility = View.INVISIBLE
			binding.averageAnswerTimeLabel.visibility = View.INVISIBLE

			binding.noDataLabel.visibility = View.VISIBLE
		}
	}
}
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
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
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
	private var mostPlayedCategoryPercent: Double = 0.0
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

				if(items!!.isNotEmpty()) {
					calculateData()
					setupCharts()
					displayData()

					binding.progressBar.visibility = View.GONE
					binding.dataContainer.visibility = View.VISIBLE

				} else {
					binding.progressBar.visibility = View.GONE
					binding.noDataLabel.visibility =  View.VISIBLE
				}
			}
		}
	}

	private fun calculateData() {
		mostPlayedCategory = items?.map { it.category }?.groupingBy { it }?.eachCount()?.maxBy { it.value }?.key!!
		mostPlayedCategoryPercent = items?.map { it.category }?.count { it == mostPlayedCategory }!!.toDouble() / items?.size!!

		val ratiosOfCorrectAnswers = items?.map { it.correctAnswers }!!.zip(items?.map { it.questions }!!) { a, b -> a.toDouble() / b }
		val answerTimes = items?.map { it.duration }!!.zip(items?.map { it.questions }!!) { a, b -> (a / 1000.0) / b }

		for(i in items!!.indices) {
			avgCorrectAnswers.add(ratiosOfCorrectAnswers.slice(0..i).average())
			avgScores.add(items?.map { it.score }!!.slice(0..i).average())
			avgAnswerTimes.add(answerTimes.slice(0..i).average())
		}
	}

	private fun setupCharts() {
		with(binding) {
			val averageCorrectAnswersChartVerticalAxis = averageCorrectAnswersChart.startAxis as VerticalAxis
			averageCorrectAnswersChartVerticalAxis.valueFormatter = DecimalFormatAxisValueFormatter("###%")
			averageCorrectAnswersChart.startAxis = averageCorrectAnswersChartVerticalAxis

			averageCorrectAnswersChart.marker = Helper.makeMarker(requireContext())


			val averageScoreChartVerticalAxis = averageScoreChart.startAxis as VerticalAxis
			averageScoreChartVerticalAxis.valueFormatter = DecimalFormatAxisValueFormatter("###,###")
			averageScoreChart.startAxis = averageScoreChartVerticalAxis

			averageScoreChart.marker = Helper.makeMarker(requireContext())


			val averageAnswerTimeChartVerticalAxis = averageAnswerTimeChart.startAxis as VerticalAxis
			averageAnswerTimeChartVerticalAxis.valueFormatter = DecimalFormatAxisValueFormatter("###,###.#")
			averageAnswerTimeChart.startAxis = averageAnswerTimeChartVerticalAxis

			averageAnswerTimeChart.marker = Helper.makeMarker(requireContext())
		}
	}

	private fun displayData() {
		binding.mostPlayedCategory.text = getString(R.string.most_played_category_text, Helper.resolveCategoryToString(mostPlayedCategory))
		binding.mostPlayedCategoryPercent.text = getString(R.string.most_played_category_percent_text, mostPlayedCategoryPercent * 100)

		binding.averageCorrectAnswers.text =
			getString(R.string.average_correct_answers_text,
			avgCorrectAnswers.last() * 100
		)
		binding.averageCorrectAnswersChart.setModel(entryModelOf(Helper.convertToListOfFloatEntries(avgCorrectAnswers)))

		binding.averageScore.text = getString(R.string.average_score_text, avgScores.last())
		binding.averageScoreChart.setModel(entryModelOf(Helper.convertToListOfFloatEntries(avgScores)))

		binding.averageAnswerTime.text = getString(R.string.average_answer_time_text, avgAnswerTimes.last())
		binding.averageAnswerTimeChart.setModel(entryModelOf(Helper.convertToListOfFloatEntries(avgAnswerTimes)))
	}
}
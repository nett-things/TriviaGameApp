package com.net3hings.triviagameapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.net3hings.triviagameapp.database.StatisticsViewModel
import com.net3hings.triviagameapp.databinding.FragmentQuestionBinding
import com.net3hings.triviagameapp.question.Question

class QuestionFragment : Fragment() {
	private lateinit var binding: FragmentQuestionBinding
	private val args: QuestionFragmentArgs by navArgs()

	private val viewModel: StatisticsViewModel by activityViewModels {
		StatisticsViewModel.StatisticsViewModelFactory(
			(activity?.application as TriviaGameApplication).repository
		)
	}

	private var currentQuestion: Int = 0
	private var correctAnswers: Int = 0
	private var score: Int = 0
	private var waitForClick: Boolean = false
	private var startTimeCount: Long = 0
	private var totalTimeElapsed: Long = 0

	private enum class Answer {
		A, B, C, D, TRUE, FALSE
	}

	private lateinit var answer: Answer

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentQuestionBinding.inflate(inflater, container, false)

		return binding.root
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// apply dark background if lights out enabled
		Helper.ifLightsOutApply(requireContext(), binding.container)

		// set up the listener for handling the go back button
		requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				AlertDialog.Builder(context)
					.setMessage(getString(R.string.quit_game_prompt))
					.setPositiveButton(getString(R.string.yes_button)) { _, _ -> findNavController().popBackStack(R.id.menuFragment, false) }
					.setNegativeButton(getString(R.string.no_button)) { _, _ -> }
					.create()
					.show()
			}
		})

		// prepare the game
		nextQuestion(initial = true)

		// set up the listeners for the answer buttons
		binding.answerACard.setOnClickListener { resolveAnswer(Answer.A) }
		binding.answerBCard.setOnClickListener { resolveAnswer(Answer.B) }
		binding.answerCCard.setOnClickListener { resolveAnswer(Answer.C) }
		binding.answerDCard.setOnClickListener { resolveAnswer(Answer.D) }
		binding.answerTrueCard.setOnClickListener { resolveAnswer(Answer.TRUE) }
		binding.answerFalseCard.setOnClickListener { resolveAnswer(Answer.FALSE) }

		// set up the listener for the continuing the game
		binding.container.setOnTouchListener { _, event ->
			if(event.action == MotionEvent.ACTION_DOWN)
				if(waitForClick) {
					waitForClick = false
					nextQuestion()
				}

			true
		}
	}

	private fun nextQuestion(initial: Boolean = false) {
		if(args.questions.size > currentQuestion + 1) {
			if(!initial) {
				currentQuestion++
				resetAnswers()
			}

			prepareAnswer()
			displayTrivia()

			startTimeCount = System.currentTimeMillis()

		} else {
			finishGame()
		}
	}

	private fun finishGame() {
		viewModel.addItem(
			args.numOfQuestions,
			args.category,
			args.difficulty,
			args.type,
			correctAnswers,
			score,
			totalTimeElapsed
		)

		findNavController().navigate(QuestionFragmentDirections.actionQuestionFragmentToEndgameFragment(
			args.numOfQuestions,
			correctAnswers,
			score
		))
	}

	private fun prepareAnswer() {
		if(args.questions[currentQuestion].type == Question.Type.MULTIPLE) {
			answer = when(('A'..'D').random()) {
				'A' -> Answer.A
				'B' -> Answer.B
				'C' -> Answer.C
				'D' -> Answer.D
				else -> Answer.A
			}

		} else {
			answer = when(args.questions[currentQuestion].correctAnswer) {
				"True" -> Answer.TRUE
				"False" -> Answer.FALSE
				else -> Answer.TRUE
			}
		}
	}

	private fun displayTrivia() {
		binding.scoreLabel.text = getString(R.string.current_score_text, score)

		binding.questionLabel.text = getString(R.string.question_label_text, currentQuestion + 1, args.questions.size)
		binding.questionContentLabel.text =
			Html.fromHtml(args.questions[currentQuestion].question, Html.FROM_HTML_MODE_COMPACT).toString()

		binding.promptLabel.text = getString(R.string.question_prompt)

		if(args.questions[currentQuestion].type == Question.Type.MULTIPLE) {
			prepareForMultipleChoice()

			when(answer) {
				Answer.A -> {
					binding.answerALabel.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}

				Answer.B -> {
					binding.answerALabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBLabel.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}

				Answer.C -> {
					binding.answerALabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCLabel.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}

				Answer.D -> {
					binding.answerALabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDLabel.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
				}

				else -> {
					binding.answerALabel.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDLabel.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}
			}

		} else {
			prepareForBooleanChoice()
		}
	}

	private fun prepareForMultipleChoice() {
		// make the boolean choice elements gone
		binding.answerTrueCard.visibility = View.GONE
		binding.answerFalseCard.visibility = View.GONE
		binding.answerFalseCardSpace.visibility = View.GONE

		// make the multiple choice elements visible
		binding.answerACard.visibility = View.VISIBLE
		binding.answerBCard.visibility = View.VISIBLE
		binding.answerCCard.visibility = View.VISIBLE
		binding.answerDCard.visibility = View.VISIBLE
		binding.answerDCardSpace.visibility = View.VISIBLE
	}

	private fun prepareForBooleanChoice() {
		// make the multiple choice elements gone
		binding.answerACard.visibility = View.GONE
		binding.answerBCard.visibility = View.GONE
		binding.answerCCard.visibility = View.GONE
		binding.answerDCard.visibility = View.GONE
		binding.answerDCardSpace.visibility = View.GONE

		// make the boolean choice elements visible
		binding.answerTrueCard.visibility = View.VISIBLE
		binding.answerFalseCard.visibility = View.VISIBLE
		binding.answerFalseCardSpace.visibility = View.VISIBLE
	}

	private fun resolveAnswer(buttonClicked: Answer) {
		// calculate elapsed time
		val timeElapsed = System.currentTimeMillis() - startTimeCount
		totalTimeElapsed += timeElapsed

		// mark all wrong
		binding.answerACard.setCardBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerBCard.setCardBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerCCard.setCardBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerDCard.setCardBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerTrueCard.setCardBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerFalseCard.setCardBackgroundColor(requireContext().getColor(R.color.red))

		// mark the correct answer
		when(answer) {
			Answer.A -> binding.answerACard.setCardBackgroundColor(requireContext().getColor(R.color.green))
			Answer.B -> binding.answerBCard.setCardBackgroundColor(requireContext().getColor(R.color.green))
			Answer.C -> binding.answerCCard.setCardBackgroundColor(requireContext().getColor(R.color.green))
			Answer.D -> binding.answerDCard.setCardBackgroundColor(requireContext().getColor(R.color.green))
			Answer.TRUE -> binding.answerTrueCard.setCardBackgroundColor(requireContext().getColor(R.color.green))
			Answer.FALSE -> binding.answerFalseCard.setCardBackgroundColor(requireContext().getColor(R.color.green))
		}

		// disable clicking
		binding.answerACard.isClickable = false
		binding.answerBCard.isClickable = false
		binding.answerCCard.isClickable = false
		binding.answerDCard.isClickable = false
		binding.answerTrueCard.isClickable = false
		binding.answerFalseCard.isClickable = false

		// update the prompt, score and correct answers count
		if(buttonClicked == answer) {
			val points = calculateScore(timeElapsed)
			score += points

			binding.promptLabel.text = getString(R.string.correct_answer_msg, points)
			binding.scoreLabel.text = getString(R.string.current_score_text, score)

			correctAnswers++

		} else
			binding.promptLabel.text = getString(R.string.wrong_answer_msg)

		// wait for click on screen to advance
		waitForClick = true
	}

	private fun calculateScore(time: Long): Int {
		val points = 100 - ((time / 1000.0) * 10).toInt()

		return if(points < 10) 10 else points
	}

	private fun resetAnswers() {
		binding.answerACard.setCardBackgroundColor(requireContext().getColor(R.color.white))
		binding.answerACard.isClickable = true

		binding.answerBCard.setCardBackgroundColor(requireContext().getColor(R.color.white))
		binding.answerBCard.isClickable = true

		binding.answerCCard.setCardBackgroundColor(requireContext().getColor(R.color.white))
		binding.answerCCard.isClickable = true

		binding.answerDCard.setCardBackgroundColor(requireContext().getColor(R.color.white))
		binding.answerDCard.isClickable = true

		binding.answerTrueCard.setCardBackgroundColor(requireContext().getColor(R.color.white))
		binding.answerTrueCard.isClickable = true

		binding.answerFalseCard.setCardBackgroundColor(requireContext().getColor(R.color.white))
		binding.answerFalseCard.isClickable = true
	}
}
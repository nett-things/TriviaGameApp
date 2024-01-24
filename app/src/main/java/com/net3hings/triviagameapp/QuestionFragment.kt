package com.net3hings.triviagameapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

	private var originalBackgroundColor: Int = 0

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

		// get the original background from a Button
		val colorStateList: ColorStateList? = binding.answerAButton.backgroundTintList
		if (colorStateList != null)
			originalBackgroundColor = colorStateList.defaultColor

		// set up the listener for handling the go back button
		requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				MaterialAlertDialogBuilder(requireContext())
					.setMessage(getString(R.string.quit_game_prompt))
					.setPositiveButton(getString(R.string.yes_button)) { _, _ -> findNavController().popBackStack(R.id.menuFragment, false) }
					.setNegativeButton(getString(R.string.no_button)) { _, _ -> }
					.show()
			}
		})

		// prepare the game
		nextQuestion(initial = true)

		// set up the listeners for the answer buttons
		binding.answerAButton.setOnClickListener { resolveAnswer(Answer.A) }
		binding.answerBButton.setOnClickListener { resolveAnswer(Answer.B) }
		binding.answerCButton.setOnClickListener { resolveAnswer(Answer.C) }
		binding.answerDButton.setOnClickListener { resolveAnswer(Answer.D) }
		binding.answerTrueButton.setOnClickListener { resolveAnswer(Answer.TRUE) }
		binding.answerFalseButton.setOnClickListener { resolveAnswer(Answer.FALSE) }

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
					binding.answerAButton.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}

				Answer.B -> {
					binding.answerAButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBButton.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}

				Answer.C -> {
					binding.answerAButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCButton.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}

				Answer.D -> {
					binding.answerAButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDButton.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
				}

				else -> {
					binding.answerAButton.text = Html.fromHtml(args.questions[currentQuestion].correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerBButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[0], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerCButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[1], Html.FROM_HTML_MODE_COMPACT).toString()
					binding.answerDButton.text = Html.fromHtml(args.questions[currentQuestion].incorrectAnswers[2], Html.FROM_HTML_MODE_COMPACT).toString()
				}
			}

		} else {
			prepareForBooleanChoice()
		}
	}

	private fun prepareForMultipleChoice() {
		// make the boolean choice elements gone
		binding.booleanChoiceContainer.visibility = View.GONE

		// make the multiple choice elements visible
		binding.multipleChoiceContainer.visibility = View.VISIBLE
	}

	private fun prepareForBooleanChoice() {
		// make the multiple choice elements gone
		binding.multipleChoiceContainer.visibility = View.GONE

		// make the boolean choice elements visible
		binding.booleanChoiceContainer.visibility = View.VISIBLE
	}

	private fun resolveAnswer(buttonClicked: Answer) {
		// calculate elapsed time
		val timeElapsed = System.currentTimeMillis() - startTimeCount
		totalTimeElapsed += timeElapsed

		// mark all wrong
		binding.answerAButton.setBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerBButton.setBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerCButton.setBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerDButton.setBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerTrueButton.setBackgroundColor(requireContext().getColor(R.color.red))
		binding.answerFalseButton.setBackgroundColor(requireContext().getColor(R.color.red))

		// mark the correct answer
		when(answer) {
			Answer.A -> binding.answerAButton.setBackgroundColor(requireContext().getColor(R.color.green))
			Answer.B -> binding.answerBButton.setBackgroundColor(requireContext().getColor(R.color.green))
			Answer.C -> binding.answerCButton.setBackgroundColor(requireContext().getColor(R.color.green))
			Answer.D -> binding.answerDButton.setBackgroundColor(requireContext().getColor(R.color.green))
			Answer.TRUE -> binding.answerTrueButton.setBackgroundColor(requireContext().getColor(R.color.green))
			Answer.FALSE -> binding.answerFalseButton.setBackgroundColor(requireContext().getColor(R.color.green))
		}

		// disable clicking
		binding.answerAButton.isClickable = false
		binding.answerBButton.isClickable = false
		binding.answerCButton.isClickable = false
		binding.answerDButton.isClickable = false
		binding.answerTrueButton.isClickable = false
		binding.answerFalseButton.isClickable = false

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
		binding.answerAButton.setBackgroundColor(originalBackgroundColor)
		binding.answerAButton.isClickable = true

		binding.answerBButton.setBackgroundColor(originalBackgroundColor)
		binding.answerBButton.isClickable = true

		binding.answerCButton.setBackgroundColor(originalBackgroundColor)
		binding.answerCButton.isClickable = true

		binding.answerDButton.setBackgroundColor(originalBackgroundColor)
		binding.answerDButton.isClickable = true

		binding.answerTrueButton.setBackgroundColor(originalBackgroundColor)
		binding.answerTrueButton.isClickable = true

		binding.answerFalseButton.setBackgroundColor(originalBackgroundColor)
		binding.answerFalseButton.isClickable = true
	}
}
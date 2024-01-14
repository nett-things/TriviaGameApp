package com.net3hings.triviagameapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.net3hings.triviagameapp.databinding.FragmentLoadingBinding
import com.net3hings.triviagameapp.network.TriviaAPI
import com.net3hings.triviagameapp.question.Question
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingFragment : Fragment() {
	private lateinit var binding: FragmentLoadingBinding
	private val args: LoadingFragmentArgs by navArgs()

	private var questions: ArrayList<Question>? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentLoadingBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// set up the listener for handling the go back button
		requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				// no action
			}
		})

		populateQuestions()
	}

	@OptIn(DelicateCoroutinesApi::class)
	fun populateQuestions() {
		GlobalScope.launch {
			try {
				questions = GlobalScope.async {
					withContext(Dispatchers.IO) {
						ArrayList(TriviaAPI.retrofitService.getQuestions(
							amount = args.numOfQuestions.toString(),
							category = args.category.toString(),
							difficulty = resolveDifficulty(args.difficulty),
							type = resolveType(args.type)
						).items)
					}
				}.await()

			} catch(e: Exception) {
				Log.e(activity?.localClassName, e.message.toString())
			}

			withContext(Dispatchers.Main) {
				if(questions != null)
					findNavController().navigate(LoadingFragmentDirections.actionLoadingFragmentToQuestionFragment(questions!!.toTypedArray()))
				else {
					Snackbar.make(binding.root, getString(R.string.cannot_load_msg), Snackbar.LENGTH_LONG).show()
					findNavController().popBackStack()
				}
			}
		}
	}

	private fun resolveDifficulty(difficulty: Question.Difficulty): String {
		return when(difficulty) {
			Question.Difficulty.ANY -> "0"
			Question.Difficulty.EASY -> "easy"
			Question.Difficulty.MEDIUM -> "medium"
			Question.Difficulty.HARD -> "hard"
		}
	}

	private fun resolveType(type: Question.Type): String {
		return when(type) {
			Question.Type.ANY -> "0"
			Question.Type.MULTIPLE -> "multiple"
			Question.Type.BOOLEAN -> "boolean"
		}
	}
}
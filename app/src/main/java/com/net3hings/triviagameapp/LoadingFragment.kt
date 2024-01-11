package com.net3hings.triviagameapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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

	private var questions: ArrayList<Question> = arrayListOf()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentLoadingBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		populateQuestions()
	}

	@OptIn(DelicateCoroutinesApi::class)
	fun populateQuestions() {
		GlobalScope.launch {
			try {
				questions = GlobalScope.async {
					withContext(Dispatchers.IO) {
						ArrayList(TriviaAPI.retrofitService.getQuestions().items)
					}
				}.await()

			} catch(e: Exception) {
				Log.e(activity?.localClassName, e.message.toString())
			}

			withContext(Dispatchers.Main) {
				findNavController().navigate(LoadingFragmentDirections.actionLoadingFragmentToQuestionFragment(questions.toTypedArray()))
			}
		}
	}
}
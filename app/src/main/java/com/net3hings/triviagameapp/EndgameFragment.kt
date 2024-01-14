package com.net3hings.triviagameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.net3hings.triviagameapp.databinding.FragmentEndgameBinding

class EndgameFragment : Fragment() {
	private lateinit var binding: FragmentEndgameBinding
	private val args: EndgameFragmentArgs by navArgs()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentEndgameBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// set up the listener for handling the go back button
		requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				findNavController().popBackStack(R.id.setupFragment, false)
			}
		})

		// display the result
		binding.resultLabel.text = getString(R.string.result_label_text, args.correctAnswers, args.numOfQuestions)

		// set up the listener for the retry button
		binding.retryButton.setOnClickListener {
			findNavController().popBackStack(R.id.setupFragment, false)
		}

		// set up the listener for the back to menu button
		binding.backToMenuButton.setOnClickListener {
			findNavController().popBackStack(R.id.menuFragment, false)
		}
	}
}
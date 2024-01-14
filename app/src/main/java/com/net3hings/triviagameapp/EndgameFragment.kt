package com.net3hings.triviagameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

		binding.resultLabel.text = getString(R.string.result_label_text, args.correctAnswers, args.numOfQuestions)
	}
}
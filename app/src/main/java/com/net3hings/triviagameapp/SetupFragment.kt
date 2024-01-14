package com.net3hings.triviagameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.net3hings.triviagameapp.databinding.FragmentSetupBinding
import com.net3hings.triviagameapp.question.Question

class SetupFragment : Fragment() {
	private lateinit var binding: FragmentSetupBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentSetupBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.numOfQuestionsLabel.text = getString(R.string.number_of_questions_label_text, binding.numberOfQuestionsSeekbar.progress)

		binding.numberOfQuestionsSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
			override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
				binding.numOfQuestionsLabel.text = getString(R.string.number_of_questions_label_text, progress)
			}

			override fun onStartTrackingTouch(seekBar: SeekBar) { }
			override fun onStopTrackingTouch(seekBar: SeekBar) { }
		})

		binding.startGameButton.setOnClickListener {
			findNavController().navigate(SetupFragmentDirections
				.actionSetupFragmentToLoadingFragment(
					binding.numberOfQuestionsSeekbar.progress,
					when(binding.categorySpinner.selectedItemPosition) {
						0 -> 0
						else -> binding.categorySpinner.selectedItemPosition + 8
					},
					Question.Difficulty.entries[binding.difficultySpinner.selectedItemPosition],
					Question.Type.entries[binding.typeSpinner.selectedItemPosition]
				)
			)
		}
	}
}
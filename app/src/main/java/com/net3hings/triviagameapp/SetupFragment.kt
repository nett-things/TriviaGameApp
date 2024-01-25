package com.net3hings.triviagameapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
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

		binding.numOfQuestionsLabel.text = getString(R.string.number_of_questions_label_text, binding.numberOfQuestionsSlider.value.toInt())

		binding.numberOfQuestionsSlider.addOnChangeListener { _, value, _ ->
			binding.numOfQuestionsLabel.text = getString(R.string.number_of_questions_label_text, value.toInt())
		}

		binding.categoryInputAutocomplete.setText(resources.getStringArray(R.array.category_array)[0], false)

		binding.startGameButton.setOnClickListener {
			findNavController().navigate(SetupFragmentDirections
				.actionSetupFragmentToLoadingFragment(
					binding.numberOfQuestionsSlider.value.toInt(),
					Helper.resolveCategoryToInt(binding.categoryInputAutocomplete.text.toString()),
					when(binding.difficultyChipGroup.checkedChipId) {
						R.id.difficulty_any_chip -> Question.Difficulty.ANY
						R.id.difficulty_easy_chip -> Question.Difficulty.EASY
						R.id.difficulty_medium_chip -> Question.Difficulty.MEDIUM
						R.id.difficulty_hard_chip -> Question.Difficulty.HARD
						else -> Question.Difficulty.ANY
					},
					when(binding.typeChipGroup.checkedChipId) {
						R.id.type_any_chip -> Question.Type.ANY
						R.id.type_multiple_choice_chip -> Question.Type.MULTIPLE
						R.id.type_true_false_chip -> Question.Type.BOOLEAN
						else -> Question.Type.ANY
					}
				)

			)
		}
	}
}
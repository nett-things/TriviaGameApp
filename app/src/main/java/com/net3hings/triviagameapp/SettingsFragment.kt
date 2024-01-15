package com.net3hings.triviagameapp

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.net3hings.triviagameapp.database.StatisticsViewModel


class SettingsFragment : PreferenceFragmentCompat() {

	private val viewModel: StatisticsViewModel by activityViewModels {
		StatisticsViewModel.StatisticsViewModelFactory(
			(activity?.application as TriviaGameApplication).repository
		)
	}

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.preferences, rootKey)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// set up the listener for handling the go back button
		requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				findNavController().popBackStack(R.id.menuFragment, false)
			}
		})

		val uiPreference: Preference? = findPreference("ui_preference")
		uiPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
			when(newValue.toString()) {
				"light_mode" -> (activity as MainActivity?)?.setThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
				"dark_mode" -> (activity as MainActivity?)?.setThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
				"device_settings" -> (activity as MainActivity?)?.setThemeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
				else -> {}
			}

			true
		}


		val button: Preference? = findPreference("clear_statistics_preference")
		button?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			viewModel.clearAll()
			true
		}
	}
}
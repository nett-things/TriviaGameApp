package com.net3hings.triviagameapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		(activity as MainActivity).showActionBar()

		return super.onCreateView(inflater, container, savedInstanceState)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// set up the listener for handling the go back button
		requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				findNavController().popBackStack(R.id.menuFragment, false)
			}
		})

		val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

		val uiPreference: Preference? = findPreference("ui_preference")
		val lightsOutPreference: Preference? = findPreference("lights_out_preference")
		val button: Preference? = findPreference("clear_statistics_preference")

		if(preferences.getString("ui_preference", "light_mode") != "light_mode")
			lightsOutPreference?.isEnabled = true

		uiPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
			(activity as MainActivity?)?.setThemeMode(newValue.toString(), preferences.getBoolean("lights_out_preference", false))

			when(newValue.toString()) {
				"dark_mode", "device_settings" -> lightsOutPreference?.isEnabled = true

				else -> lightsOutPreference?.isEnabled = false // light_mode
			}

			true
		}

		lightsOutPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
			(activity as MainActivity?)?.setThemeMode(preferences.getString("ui_preference", "light_mode")!!, newValue.toString().toBoolean())

			activity?.recreate()

			true
		}

		button?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			MaterialAlertDialogBuilder(requireContext())
				.setMessage(getString(R.string.clear_statistics_prompt))
				.setPositiveButton(getString(R.string.confirm_button)) { _, _ -> viewModel.clearAll() }
				.setNeutralButton(getString(R.string.cancel_button)) { _, _ -> }
				.show()

			true
		}
	}
}
package com.net3hings.triviagameapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.net3hings.triviagameapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val preferences = PreferenceManager.getDefaultSharedPreferences(this)
		when(preferences.getString("ui_preference", "light_mode")) {
			"light_mode" -> setThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
			"dark_mode" -> setThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
			"device_settings" -> setThemeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
		}

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	fun setThemeMode(theme: Int) {
		when(theme) {
			AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(theme)
			AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(theme)
			AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> AppCompatDelegate.setDefaultNightMode(theme)
			else -> {}
		}
	}
}
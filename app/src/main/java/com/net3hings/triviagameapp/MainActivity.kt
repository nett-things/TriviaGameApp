package com.net3hings.triviagameapp

import android.content.res.Configuration
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

		setThemeMode(
			preferences.getString("ui_preference", "light_mode")!!,
			preferences.getBoolean("lights_out_preference", false)
		)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	fun setThemeMode(mode: String, lightsOut: Boolean) {
		when(mode) {
			"light_mode" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

			"dark_mode" -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

				if(lightsOut)
					window.statusBarColor = getColor(R.color.black)
			}

			"device_settings" -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

				if(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
					if(lightsOut)
						window.statusBarColor = getColor(R.color.black)
			}
		}
	}
}
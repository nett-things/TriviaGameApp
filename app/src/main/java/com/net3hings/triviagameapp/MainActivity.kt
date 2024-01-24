package com.net3hings.triviagameapp

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.net3hings.triviagameapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	private lateinit var navController: NavController
	private lateinit var appBarConfiguration: AppBarConfiguration

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val preferences = PreferenceManager.getDefaultSharedPreferences(this)

		setThemeMode(
			preferences.getString("ui_preference", "light_mode")!!,
			preferences.getBoolean("lights_out_preference", false)
		)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setupActionBar()
	}

	fun setThemeMode(mode: String, lightsOut: Boolean) {
		when(mode) {
			"light_mode" -> {
				setTheme(R.style.Base_Theme_TriviaGameApp)
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}

			"dark_mode" -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

				if(lightsOut)
					setTheme(R.style.Base_Theme_TriviaGameAppAmoled)
				else
					setTheme(R.style.Base_Theme_TriviaGameApp)
			}

			"device_settings" -> {
				if(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES && lightsOut)
					setTheme(R.style.Base_Theme_TriviaGameAppAmoled)
				else
					setTheme(R.style.Base_Theme_TriviaGameApp)

				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
			}
		}
	}

	@SuppressLint("RestrictedApi")
	fun showActionBar() {
		supportActionBar?.setShowHideAnimationEnabled(false)
		supportActionBar?.show()
	}

	@SuppressLint("RestrictedApi")
	fun hideActionBar() {
		supportActionBar?.setShowHideAnimationEnabled(false)
		supportActionBar?.hide()
	}

	private fun setupActionBar() {
		navController = findNavController(R.id.nav_host_fragment_container)
		appBarConfiguration = AppBarConfiguration(navController.graph)
		setupActionBarWithNavController(navController, appBarConfiguration)

		supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.transparent)))
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)

		return true
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
	}
}
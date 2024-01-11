package com.net3hings.triviagameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.net3hings.triviagameapp.databinding.FragmentSetupBinding

class SetupFragment : Fragment() {
	private lateinit var binding: FragmentSetupBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentSetupBinding.inflate(inflater, container, false)

		return binding.root
	}
}
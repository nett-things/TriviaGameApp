package com.net3hings.triviagameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.net3hings.triviagameapp.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
	private lateinit var binding: FragmentMenuBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentMenuBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.startGameButton.setOnClickListener {
			findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToLoadingFragment())
		}
	}
}
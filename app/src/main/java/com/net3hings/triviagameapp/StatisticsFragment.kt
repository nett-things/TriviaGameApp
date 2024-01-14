package com.net3hings.triviagameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.net3hings.triviagameapp.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {
	private lateinit var binding: FragmentStatisticsBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentStatisticsBinding.inflate(inflater, container, false)

		return binding.root
	}
}
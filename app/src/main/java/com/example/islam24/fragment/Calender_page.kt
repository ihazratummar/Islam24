package com.example.islam24.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.islam24.MainActivity
import com.example.islam24.R

class CalenderPageFragment : Fragment() {
    private var onBackPressedCallback: OnBackPressedCallback? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupOnBackPressed()
        return inflater.inflate(R.layout.fragment_calender_page, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

    private fun setupOnBackPressed() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    // Handle back press
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }

        // Add the onBackPressedCallback to the onBackPressedDispatcher
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)
    }


}
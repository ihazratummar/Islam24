package com.hazrat.islam24.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.hazrat.islam24.MainActivity
import com.hazrat.islam24.R

class Tasbih : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setupOnBackPressed()
        val view = inflater.inflate(R.layout.fragment_tasbih, container, false)

        (activity as? MainActivity)?.hideBottomNavigationView()

    return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

    private fun setupOnBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(isEnabled){
//                    Toast.makeText(requireContext(),"GoBack",Toast.LENGTH_SHORT).show()
                    isEnabled =false
                    requireActivity().onBackPressed()
                }
            }
        })


    }


}
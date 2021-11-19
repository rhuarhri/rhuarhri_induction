package com.three_squared.rhuarhri_induction.user_screen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.databinding.SearchScreenFragmentBinding
import com.three_squared.rhuarhri_induction.databinding.UserScreenFragmentBinding

class UserScreenFragment : Fragment() {

    private lateinit var binding: UserScreenFragmentBinding
    private lateinit var viewModel: UserScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root

        //return inflater.inflate(R.layout.user_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserScreenViewModel::class.java)

        binding.viewmodel = viewModel
        binding.button3.setOnClickListener {
            findNavController().navigate(R.id.action_userScreenFragment_to_viewCommitScreenFragment)
        }
    }

}
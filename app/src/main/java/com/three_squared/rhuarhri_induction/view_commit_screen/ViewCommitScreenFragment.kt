package com.three_squared.rhuarhri_induction.view_commit_screen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.databinding.ViewCommitScreenFragmentBinding

class ViewCommitScreenFragment : Fragment() {

    private lateinit var binding: ViewCommitScreenFragmentBinding
    private lateinit var viewModel: ViewCommitScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewCommitScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root

        //return inflater.inflate(R.layout.view_commit_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewCommitScreenViewModel::class.java)

        binding.viewmodel = viewModel
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_viewCommitScreenFragment_to_userScreenFragment)
        }
    }

}
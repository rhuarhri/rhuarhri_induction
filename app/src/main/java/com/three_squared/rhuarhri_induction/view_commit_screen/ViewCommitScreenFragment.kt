package com.three_squared.rhuarhri_induction.view_commit_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.databinding.ViewCommitScreenFragmentBinding

class ViewCommitScreenFragment : Fragment() {

    private lateinit var binding: ViewCommitScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewCommitScreenFragmentBinding.inflate(inflater, container, false)

        val viewModel: ViewCommitScreenViewModel by viewModels() //ViewModelProvider(this).get(ViewCommitScreenViewModel::class.java)

        binding.viewmodel = viewModel

        viewModel.setup(arguments)

        val repositoryObserver = Observer<Repository> {
            binding.descriptionTXT.text = it.description
            binding.repoNameTXT.text = it.name
        }

        viewModel.repository.observe(viewLifecycleOwner, repositoryObserver)

        return binding.root

        //return inflater.inflate(R.layout.view_commit_screen_fragment, container, false)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        *//*binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_viewCommitScreenFragment_to_userScreenFragment)
        }*//*
    }*/

}
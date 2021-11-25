package com.three_squared.rhuarhri_induction.view_commit_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.databinding.ViewCommitScreenFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        binding.commitsRV.setHasFixedSize(true)

        val commitListObserver = Observer<List<Commit>> { commitList ->
            binding.commitsRV.adapter = CommitListAdapter(commitList) { commit ->
                println("commit message was ${commit.message}")

                val commitMessages : MutableList<String> = mutableListOf()

                for (commitItem in commitList) {
                    commitMessages.add(commitItem.message)
                }

                val data = bundleOf(
                    "id" to commit.committerId,
                    "name" to commit.committerName,
                    "avatar" to commit.committerAvatar,
                    "commitMessages" to commitMessages
                )
                findNavController().navigate(R.id.action_viewCommitScreenFragment_to_userScreenFragment, data)
            }
        }

        viewModel.commitList.observe(viewLifecycleOwner, commitListObserver)

        binding.commitsRV.layoutManager = LinearLayoutManager(this.context)

        /*binding.button.setOnClickListener {
            viewModel.getCommits(viewModel.getRepositoryOwnerName(), viewModel.repository.value?.name ?: "")
        }*/

        viewModel.getCommits(viewModel.getRepositoryOwnerName(), viewModel.repository.value?.name ?: "")

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
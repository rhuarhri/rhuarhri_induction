package com.three_squared.rhuarhri_induction.search_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.databinding.SearchScreenFragmentBinding
import com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments.RepoListFragment
import com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments.RepositoryParcelable
import com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments.SearchFragment

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchScreenFragment : Fragment() {

    private lateinit var binding: SearchScreenFragmentBinding
    private lateinit var viewModel: SearchScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SearchScreenFragmentBinding.inflate(inflater, container, false)

        val newViewModel : SearchScreenViewModel by viewModels()

        viewModel = newViewModel

        binding.viewmodel = viewModel

        setupSearchFragment("", "")
        setupRepoList(listOf())

        val userInfoObserver = Observer<User> { user ->
            setupSearchFragment(user.name, user.avatar)
        }
        viewModel.userInfo.observe(viewLifecycleOwner, userInfoObserver)

        val repoListObserver = Observer<List<Repository>> { repoList ->
            setupRepoList(repoList)
        }

        viewModel.repositoryList.observe(viewLifecycleOwner, repoListObserver)

        return binding.root

        //return inflater.inflate(R.layout.search_screen_fragment, container, false)
    }

    private fun setupSearchFragment(name : String, avatarUrl : String) {
        val searchFragment = SearchFragment.newInstance(name, avatarUrl)

        val fragmentManger : FragmentManager = this.childFragmentManager
        fragmentManger.beginTransaction().apply {
            replace(binding.SearchFragmentLocation.id, searchFragment)
            commit()
        }
    }

    private fun setupRepoList(repositories: List<Repository>) {
        val repoParcelableList = mutableListOf<RepositoryParcelable>()

        repositories.forEach { repo ->
            val newParcelable = RepositoryParcelable(repo.id, repo.name, repo.visibility, repo.description)

            repoParcelableList.add(newParcelable)
        }

        val parcelableRepoList : ArrayList<RepositoryParcelable> = ArrayList()
        parcelableRepoList.addAll(repoParcelableList)

        val repoListFragment = RepoListFragment.newInstance(parcelableRepoList)

        val fragmentManger : FragmentManager = this.childFragmentManager

        fragmentManger.beginTransaction().apply {
            replace(binding.RepoInfoLocation.id, repoListFragment)
            commit()
        }
    }

    fun onSearch(name : String) {
        viewModel.searchForUser(name)
    }

    fun onRefresh() {
        val user = viewModel.userInfo.value
        if (user != null) {
            val name = user.name
            if (name.isNotBlank()) {
                viewModel.searchForUser(name)
            }
        }
    }

    fun onItemClicked(repository : Repository) {

        val userName : String = viewModel.userInfo.value?.name ?: ""

        println("user name is $userName")
        println("repository id ${repository.id}")
        println("repository name ${repository.name}")

        val data = bundleOf(
            "ownerName" to userName,
            "id" to repository.id,
            "name" to repository.name,
            "description" to repository.description,
            "visibility" to repository.visibility
        )
        findNavController().navigate(R.id.action_searchScreenFragment_to_viewCommitScreenFragment, data)
    }

}
package com.three_squared.rhuarhri_induction.search_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
        val searchFragment = SearchFragment.newInstance(name)

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

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /*
        recycler view set up
        binding.searchResultRV.setHasFixedSize(true)
        binding.searchResultRV.adapter = SearchListAdapter(viewModel.searchListResult) { name ->
            println(name)
        }

        binding.searchResultRV.layoutManager = LinearLayoutManager(this.context)

        binding.button.setOnClickListener {
            viewModel.searchGithub()
        }*/

        /*binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_searchScreenFragment_to_viewCommitScreenFragment)
        }*/
    }*/

    fun onSearch(name : String) {
        println("search for user with name of $name")
        viewModel.searchForUser(name)
    }

    fun onItemClicked(id : String, name : String) {
        println("repo item name is $name")
        println("repo item id is $id")
    }

}
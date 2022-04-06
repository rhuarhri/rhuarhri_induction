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
import com.google.android.material.snackbar.Snackbar
import com.three_squared.rhuarhri_induction.BuildConfig
import com.three_squared.rhuarhri_induction.MainActivity
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

    /*TODO App Presentation slide 8
    In this screen the user will search for a list of repositories based
    on the name of a user.

     */

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

        setupSearchFragment("", "", false)
        setupRepoList(listOf(), "")

        val mainViewModel = (activity as MainActivity).mainActivityViewModel

        val userInfoObserver = Observer<User> { user ->
            setupSearchFragment(user.name, user.avatar, viewModel.loading.value ?: false)
        }
        mainViewModel.userInfo.observe(viewLifecycleOwner, userInfoObserver)

        val loadingObserver = Observer<Boolean> { loading ->
            var name = ""
            var avatarUrl = ""

            val currentUser = mainViewModel.userInfo.value
            if (currentUser != null) {
                name = currentUser.name
                avatarUrl = currentUser.avatar
            }

            setupSearchFragment(name, avatarUrl, loading)
        }

        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)

        val repoListObserver = Observer<List<Repository>> { repoList ->
            setupRepoList(repoList, mainViewModel.errorMessage.value ?: "")
        }




        mainViewModel.repositoryList.observe(viewLifecycleOwner, repoListObserver)

        val errorMessageObserver = Observer<String> { errorMessage ->
            setupRepoList(mainViewModel.repositoryList.value ?: listOf(), errorMessage)
        }

        mainViewModel.errorMessage.observe(viewLifecycleOwner, errorMessageObserver)

        return binding.root
    }

    private fun setupSearchFragment(name : String, avatarUrl : String, loading : Boolean) {
        /*replace the search bar with new search bar instance containing the
        new information
         */
        val searchFragment = SearchFragment.newInstance(name, avatarUrl, loading)

        val fragmentManger : FragmentManager = this.childFragmentManager
        fragmentManger.beginTransaction().apply {
            replace(binding.SearchFragmentLocation.id, searchFragment)
            commit()
        }
    }

    private fun setupRepoList(repositories: List<Repository>, errorMessage : String) {
        /*replace the repository list with new repository list instance containing the
        new information
         */
        val repoParcelableList = mutableListOf<RepositoryParcelable>()

        repositories.forEach { repo ->
            val newParcelable = RepositoryParcelable(repo.id, repo.name, repo.visibility, repo.description)

            repoParcelableList.add(newParcelable)
        }

        val parcelableRepoList : ArrayList<RepositoryParcelable> = ArrayList()
        parcelableRepoList.addAll(repoParcelableList)

        val repoListFragment = RepoListFragment.newInstance(parcelableRepoList, errorMessage)

        val fragmentManger : FragmentManager = this.childFragmentManager

        fragmentManger.beginTransaction().apply {
            replace(binding.RepoInfoLocation.id, repoListFragment)
            commit()
        }
    }

    fun onSearch(name : String) {
        /*when the search button is pressed*/

        val bitriseValue = BuildConfig.Bitrise_Env

        Snackbar.make(binding.root, "received value from bitrise was ($bitriseValue)", Snackbar.LENGTH_LONG).show()

        viewModel.searchForUser(name) {
            val mainViewModel = (activity as MainActivity).mainActivityViewModel
            mainViewModel.setUserInfo(it)
        }
    }

    fun onRefresh() {
        /*When the user drags to refresh the repository list*/
        val user = (activity as MainActivity).mainActivityViewModel.userInfo.value
        if (user != null) {
            val name = user.name
            if (name.isNotBlank()) {
                viewModel.searchForUser(name) {
                    val mainViewModel = (activity as MainActivity).mainActivityViewModel
                    mainViewModel.setUserInfo(it)
                }
            }
        }
    }

    fun onItemClicked(repository : Repository) {
        /*when a repository in the repository list is pressed*/
        val userName : String = (activity as MainActivity).mainActivityViewModel.userInfo.value?.name ?: ""

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
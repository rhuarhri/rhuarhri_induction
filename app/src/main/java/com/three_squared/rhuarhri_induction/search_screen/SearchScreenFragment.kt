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

    /*TODO App Presentation 3 Search Screen
    In this screen the user will search for a list of repositories based
    on the name of a user.

    Looking back
    The Main problem with this screen is that the UI could be simplified.
    At the moment this screen is split into two chunks one for the chunks being the
    search bar and the other being the repository list. Both of these chunks
    have a landscape and portrait variant.
    Now the landscape and portrait variant of the repository list look
    the same so one variant could be removed.

    It also has some other problems but they also apply to the other screens.
    The app does not inform the user that an error has occurred. For example
    on this screen if the user is searching for a repository at the app fails then
    the user will just get a blank list.

    The second problem is the app does not have a limit to how much information
    it can show. For example if the user finds someone who has 1000 repositories
    the app will display all 1000. This can be a problem as the app would have to
    use resources to display repositories that the user will not see.

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
    }

    private fun setupSearchFragment(name : String, avatarUrl : String) {
        /*replace the search bar with new search bar instance containing the
        new information
         */
        val searchFragment = SearchFragment.newInstance(name, avatarUrl)

        val fragmentManger : FragmentManager = this.childFragmentManager
        fragmentManger.beginTransaction().apply {
            replace(binding.SearchFragmentLocation.id, searchFragment)
            commit()
        }
    }

    private fun setupRepoList(repositories: List<Repository>) {
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

        val repoListFragment = RepoListFragment.newInstance(parcelableRepoList, "ERROR")

        val fragmentManger : FragmentManager = this.childFragmentManager

        fragmentManger.beginTransaction().apply {
            replace(binding.RepoInfoLocation.id, repoListFragment)
            commit()
        }
    }

    fun onSearch(name : String) {
        /*when the search button is pressed*/
        viewModel.searchForUser(name)
    }

    fun onRefresh() {
        /*When the user drags to refresh the repository list*/
        val user = viewModel.userInfo.value
        if (user != null) {
            val name = user.name
            if (name.isNotBlank()) {
                viewModel.searchForUser(name)
            }
        }
    }

    fun onItemClicked(repository : Repository) {
        /*when a repository in the repository list is pressed*/
        val userName : String = viewModel.userInfo.value?.name ?: ""

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
package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
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

        val searchFragment = SearchFragment.newInstance("Rhuarhri Cordon")

        val parcelableRepoList : ArrayList<RepositoryParcelable> = arrayListOf<RepositoryParcelable>(
            RepositoryParcelable(id = "1", name = "Project 1", visibility = "Public", description = "Test"),
            RepositoryParcelable(id = "2", name = "Project 2", visibility = "Public", description = "Test")
        )

        val repoListFragment = RepoListFragment.newInstance(parcelableRepoList)

        val fragmentManger : FragmentManager = this.childFragmentManager

        fragmentManger.beginTransaction().apply {
            replace(binding.SearchFragmentLocation.id, searchFragment)
            replace(binding.RepoInfoLocation.id, repoListFragment)
            commit()
        }

        return binding.root

        //return inflater.inflate(R.layout.search_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val newViewModel : SearchScreenViewModel by viewModels()//ViewModelProvider(this).get(SearchScreenViewModel::class.java)

        viewModel = newViewModel

        binding.viewmodel = viewModel

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
    }

    public fun onSearch(name : String) {
        println("search for user with name of $name")
        viewModel.searchForUser(name)
    }

    public fun onItemClicked(id : String, name : String) {
        println("repo item name is $name")
        println("repo item id is $id")
    }

}
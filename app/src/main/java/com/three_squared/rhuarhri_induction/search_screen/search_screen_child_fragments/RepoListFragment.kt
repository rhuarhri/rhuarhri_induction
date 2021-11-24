package com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.databinding.FragmentRepoListBinding
import com.three_squared.rhuarhri_induction.search_screen.SearchListAdapter
import com.three_squared.rhuarhri_induction.search_screen.SearchScreenFragment

class RepoListFragment : Fragment() {

    private lateinit var binding: FragmentRepoListBinding
    private var repoList : ArrayList<RepositoryParcelable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repoList = it.getParcelableArrayList(searchResultKey)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRepoListBinding.inflate(inflater, container, false)

        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_repo_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //recycler view set up
        binding.searchResultRV.setHasFixedSize(true)

        val displayedRepoList : MutableList<Repository> = mutableListOf()
        repoList?.forEach { parcelableRepo ->
            if (parcelableRepo.id != null && parcelableRepo.name != null
                && parcelableRepo.visibility != null && parcelableRepo.description != null) {
                val foundRepository = Repository(parcelableRepo.id, parcelableRepo.name,
                parcelableRepo.visibility, parcelableRepo.description)
                displayedRepoList.add(foundRepository)
            }
        }

        binding.searchResultRV.adapter = SearchListAdapter(displayedRepoList) { repository ->
            (parentFragment as SearchScreenFragment?)?.onItemClicked(repository)
        }

        binding.searchResultRV.layoutManager = LinearLayoutManager(this.context)

    }

    companion object {
        const val searchResultKey : String = "Result"

        @JvmStatic
        fun newInstance(repoList : ArrayList<RepositoryParcelable>) =
            RepoListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(searchResultKey, repoList)
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)

                }
            }
    }
}
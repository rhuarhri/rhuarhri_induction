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
    private var errorMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repoList = it.getParcelableArrayList(searchResultKey)
            errorMessage = it.getString(errorMessageKey, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRepoListBinding.inflate(inflater, container, false)

        //setup error message
        binding.errorMessageView.setContent {
            val errorMessageWidget = ErrorMessageWidget()
            errorMessageWidget.errorMessage(message = errorMessage)
        }

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

        binding.searchResultListSRL.setOnRefreshListener {
            (parentFragment as SearchScreenFragment?)?.onRefresh()
        }

        return binding.root
    }

    companion object {
        const val searchResultKey : String = "Result"
        const val errorMessageKey : String = "Error"

        @JvmStatic
        fun newInstance(repoList : ArrayList<RepositoryParcelable>, errorMessage : String) =
            RepoListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(searchResultKey, repoList)
                    putString(errorMessageKey, errorMessage)
                }
            }
    }
}
package com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.three_squared.rhuarhri_induction.databinding.FragmentSearchBinding
import com.three_squared.rhuarhri_induction.search_screen.SearchScreenFragment

class SearchFragment : Fragment() {

    private var name : String? = null
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(nameKey)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.searchBTN.setOnClickListener {
            val name = binding.searchTextInputET.text.toString()

            (parentFragment as SearchScreenFragment?)?.onSearch(name)
        }

        binding.name = if (name == null) {
            ""
        } else {
            name
        }
    }

    companion object {

        const val nameKey = "name"

        @JvmStatic
        fun newInstance(name: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(nameKey, name)
                }
            }
    }
}
package com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.three_squared.rhuarhri_induction.databinding.FragmentSearchBinding
import com.three_squared.rhuarhri_induction.search_screen.SearchScreenFragment

class SearchFragment : Fragment() {

    private var name : String? = null
    private var avatarURL : String? = null
    private var loading : Boolean = false
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(nameKey)
            avatarURL = it.getString(avatarURLKey)
            loading = it.getBoolean(loadingKey)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        /*binding.searchBTN.setOnClickListener {
            val name = binding.searchTextInputET.text.toString()

            (parentFragment as SearchScreenFragment?)?.onSearch(name)
        }*/

        binding.searchButtonView.setContent {
            val searchButtonWidget = SearchButtonWidget()

            val buttonState = if (!loading) {
                SearchButtonState.ENABLED
            } else {
                SearchButtonState.LOADING
            }

            searchButtonWidget.searchButton(state = buttonState) {
                val name = binding.searchTextInputET.text.toString()

                (parentFragment as SearchScreenFragment?)?.onSearch(name)
            }
        }

        binding.name = if (name == null) {
            ""
        } else {
            name
        }

        if (!avatarURL.isNullOrBlank()) {

            Glide.with(binding.root).load(avatarURL).into(binding.foundUserImageIV)
        }

        return binding.root

    }

    companion object {

        const val nameKey = "name"
        const val avatarURLKey = "avatar"
        const val loadingKey = "loading"

        @JvmStatic
        fun newInstance(name: String, avatarURL : String, loading : Boolean) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(nameKey, name)
                    putString(avatarURLKey, avatarURL)
                    putBoolean(loadingKey, loading)
                }
            }
    }
}
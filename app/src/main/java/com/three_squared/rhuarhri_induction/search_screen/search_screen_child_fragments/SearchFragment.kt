package com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.three_squared.rhuarhri_induction.databinding.FragmentSearchBinding
import com.three_squared.rhuarhri_induction.search_screen.SearchScreenFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

class SearchFragment : Fragment() {

    private var name : String? = null
    private var avatarURL : String? = null
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(nameKey)
            avatarURL = it.getString(avatarURLKey)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchBTN.setOnClickListener {
            val name = binding.searchTextInputET.text.toString()

            (parentFragment as SearchScreenFragment?)?.onSearch(name)
        }

        binding.name = if (name == null) {
            ""
        } else {
            name
        }

        if (!avatarURL.isNullOrBlank()) {
            /*GlobalScope.launch {
                println("avatar url is $avatarURL")
                val bitmap : Bitmap = BitmapFactory.decodeStream(URL(avatarURL).content as InputStream)
                withContext(Dispatchers.Main) {
                    binding.foundUserImageIV.setImageBitmap(bitmap)
                }
            }*/

            Glide.with(binding.root).load(avatarURL).into(binding.foundUserImageIV)
        }

        return binding.root

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }*/

    companion object {

        const val nameKey = "name"
        const val avatarURLKey = "avatar"

        @JvmStatic
        fun newInstance(name: String, avatarURL : String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(nameKey, name)
                    putString(avatarURLKey, avatarURL)
                }
            }
    }
}
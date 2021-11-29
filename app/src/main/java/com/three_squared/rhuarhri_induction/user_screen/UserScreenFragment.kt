package com.three_squared.rhuarhri_induction.user_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.databinding.UserScreenFragmentBinding

class UserScreenFragment : Fragment() {

    private lateinit var binding: UserScreenFragmentBinding
    //private lateinit var viewModel: UserScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserScreenFragmentBinding.inflate(inflater, container, false)

        val viewModel : UserScreenViewModel by viewModels()

        binding.viewmodel = viewModel

        val userObserver = Observer<User> { user ->
            binding.userNameTXT.text = user.name

            if (user.avatar.isNotBlank()) {
                Glide.with(binding.root).load(user.avatar).into(binding.userAvatarIV)
            }
        }

        viewModel.user.observe(viewLifecycleOwner, userObserver)

        binding.userCommitListRV.setHasFixedSize(true)
        val commitListObserver = Observer<List<Commit>> { commitList ->
            binding.userCommitListRV.adapter = UsersCommitListAdapter(commitList)
        }

        viewModel.commitList.observe(viewLifecycleOwner, commitListObserver)

        binding.seeRepoBTN.setOnClickListener {
            val user = viewModel.user.value ?: User("","","", "", listOf())

            val data = bundleOf(
                "id" to user.id,
                "name" to user.name
            )

            findNavController().navigate(R.id.action_userScreenFragment_to_searchScreenFragment, data)
        }

        binding.userCommitListRV.layoutManager = LinearLayoutManager(this.context)

        viewModel.setup(arguments)

        return binding.root

        //return inflater.inflate(R.layout.user_screen_fragment, container, false)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserScreenViewModel::class.java)

        binding.viewmodel = viewModel

    }*/

    /*fun toUserSearchScreen(user : User) {
        val data = bundleOf(
            "id" to user.id,
            "name" to user.name
        )

        findNavController().navigate(R.id.action_userScreenFragment_to_searchScreenFragment, data)
    }*/

    /*fun toCommitScreen(repository : Repository) {
        val data = bundleOf(
            "id" to repository.id,
            "name" to repository.name,
            "description" to repository.description,
            "visibility" to repository.visibility
        )
        findNavController().navigate(R.id.action_searchScreenFragment_to_viewCommitScreenFragment, data)
    }*/

}
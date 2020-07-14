package com.rasyidin.githubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.adapter.ListUserAdapter
import com.rasyidin.githubuser.model.User
import com.rasyidin.githubuser.viewmodel.FollowersViewModel
import com.rasyidin.githubuser.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_followers_and_following.*

class FollowersAndFollowingFragment : Fragment() {

    private var user: User? = null

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        const val EXTRA_USERNAME = "extra_username"

        fun newInstance(index: Int): FollowersAndFollowingFragment {
            val fragment = FollowersAndFollowingFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var adapter: ListUserAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers_and_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var index = 1
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }

        when (index) {
            1 -> {
                showListFollowers()
                linkFollowersViewModel()
            }
            else -> {
                showListFollowers()
                linkFollowingViewModel()
            }
        }
    }

    private fun showListFollowers() {
        adapter = ListUserAdapter()

        rvListFollowers.layoutManager = LinearLayoutManager(context)
        rvListFollowers.adapter = adapter
        rvListFollowers.setHasFixedSize(true)
    }

    private fun linkFollowersViewModel() {
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        user = activity?.intent?.getParcelableExtra(EXTRA_USERNAME) as User
        showLoading(true)
        followersViewModel.setFollowersUser(user!!.login!!)

        followersViewModel.getFollowersUser()
            .observe(viewLifecycleOwner, Observer { followersItem ->
                if (followersItem != null) {
                    adapter.setData(followersItem)
                    showLoading(false)
                }
            })
    }

    private fun linkFollowingViewModel() {
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        user = activity?.intent?.getParcelableExtra(EXTRA_USERNAME) as User
        followingViewModel.setFollowingUser(user!!.login!!)

        followingViewModel.getFollowingUser()
            .observe(viewLifecycleOwner, Observer { followingItem ->
                if (followingItem != null) {
                    adapter.setData(followingItem)
                    showLoading(false)
                }
            })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
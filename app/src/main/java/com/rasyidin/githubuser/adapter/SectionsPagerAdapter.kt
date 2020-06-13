package com.rasyidin.githubuser.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.ui.fragment.FollowersAndFollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitle =
        intArrayOf(R.string.tab_title_1, R.string.tab_title_2)

    override fun getItem(position: Int): Fragment {
        return FollowersAndFollowingFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }

    override fun getCount(): Int {
        return 2
    }


}
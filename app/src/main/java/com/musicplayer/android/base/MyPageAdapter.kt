package com.musicplayer.android.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var fragmentList = mutableListOf<Fragment>()
    private var titles = mutableListOf<String>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getTabTitle(position: Int): String {
        return titles[position]
    }



    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titles.add(title)
    }
}

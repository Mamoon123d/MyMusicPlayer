package com.musicplayer.android.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.activity.SettingsPage
import com.musicplayer.android.adapter.MeCategoryAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.FragmentMeBinding
import com.musicplayer.android.model.MeCategoryData

class MeFragment : BaseFragment<FragmentMeBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun initM() {
        binding.settings.setOnClickListener {
            goActivity(SettingsPage())
        }
        setContent()
    }

    private fun setContent() {
        val data = ArrayList<MeCategoryData>()
        /* for (i in 1..20) {
             data.add(MeCategoryData( "Item $i", R.drawable.youtube_cover))
         }*/
        data.add(MeCategoryData("Downloads", R.drawable.download))
        data.add(MeCategoryData("MP3 Converter", R.drawable.mp3))
        data.add(MeCategoryData("Privacy", R.drawable.feedback))
        data.add(MeCategoryData("History", R.drawable.history))
        data.add(MeCategoryData("Media Manage", R.drawable.rate_us))
        val adapter = MeCategoryAdapter(mActivity, data)
        // val categoryRV = binding.rvMe

        binding.rvMe.apply {
            this.adapter = adapter.apply {
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        if (position == 3) {
                            goActivity(History())
                        }
                    }
                })
            }
            layoutManager = GridLayoutManager(mActivity, 3)
        }


    }
}
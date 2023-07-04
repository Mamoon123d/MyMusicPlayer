package com.musicplayer.android.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.adapter.SettingAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.SettingsPageBinding
import com.musicplayer.android.model.SettingData

class SettingsPage : BaseActivity<SettingsPageBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.settings_page
    }

    override fun initM() {
        settings()
    }

    private fun settings() {
        val list = mutableListOf<SettingData>()
        list.add(SettingData(1, R.drawable.ic_mobile, "General", "Language,History"))
        list.add(
            SettingData(
                2,
                R.drawable.player_play,
                "Video",
                "Screen orientation,continuous playing "
            )
        )
        list.add(SettingData(3, R.drawable.ic_headphone, "Audio", "Audio format,audio duration"))
        list.add(
            SettingData(
                4,
                R.drawable.ic_info,
                "About us",
                "Fans group,official website,pages & channel"
            )
        )
        binding.rvSetting.apply {
            adapter = SettingAdapter(mActivity, list).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        showMsg(list[position].title)
                    }
                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }
}
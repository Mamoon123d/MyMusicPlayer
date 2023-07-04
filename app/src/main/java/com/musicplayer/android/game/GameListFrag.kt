package com.musicplayer.android.game

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.GameListFragBinding
import com.musicplayer.android.utils.MyIntent

class GameListFrag : BaseFragment<GameListFragBinding>() {

    override fun setLayoutId(): Int {
        return R.layout.game_list_frag
    }

    override fun initM() {
        val list = GameFrag.list
        binding.rvGame.apply {
            adapter = GameListAdapter(mActivity, list).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        MyIntent.goGameIntent(mActivity, "https://games.cdn.famobi.com/html5games/s/smarty-bubbles-2/v200/?fg_domain=play.famobi.com&fg_aid=A1000-111&fg_uid=22d86193-c681-45c4-9f4d-8d5aab731603&fg_pid=e37ab3ce-88cd-4438-9b9c-a37df5d33736&fg_beat=385&original_ref=https%3A%2F%2Fplay.famobi.com%2Fsmarty-bubbles-2%2FA-FAMOBI-COM")

                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }
}
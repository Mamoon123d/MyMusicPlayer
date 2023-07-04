package com.musicplayer.android.offer

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musicplayer.android.R
import com.musicplayer.android.adapter.RewardAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.RewardsBinding
import com.musicplayer.android.model.RewardData

class Rewards : BaseActivity<RewardsBinding>() {

    override fun setLayoutId(): Int {
        return R.layout.rewards
    }

    override fun initM() {
        setRewards()

        setTb()
    }

    private fun setTb() {
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun setRewards() {
        val list = mutableListOf<RewardData>()
        list.add(
            RewardData(
                1,
                "Paytm",
                "Redeem for\n Cashback",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Paytm_logo.png/640px-Paytm_logo.png",
                "#76CFF8"
            )
        )
        list.add(
            RewardData(
                2,
                "FreeFireMAX",
                "Redeem for\n Diamonds",
                "https://cdn2.downdetector.com/static/uploads/logo/freefire.png",
                "#f1c232"
            )
        )
        list.add(
            RewardData(
                3,
                "PUBG Game",
                "Redeem for\n UC's",
                "https://pngimg.com/d/pubg_PNG33.png",
                "#ffd966"
            )
        )
        list.add(
            RewardData(
                4,
                "PayPal",
                "Redeem for\n Cashback",
                "https://assets.stickpng.com/images/580b57fcd9996e24bc43c530.png",
                "#76CFF8"
            )
        )

        binding.rvReward.apply {
            adapter = RewardAdapter(mActivity, list).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        goActivity(RedeemSubmit())
                    }

                })
            }
            layoutManager = GridLayoutManager(mActivity, 2, RecyclerView.VERTICAL, false)
        }
    }

}

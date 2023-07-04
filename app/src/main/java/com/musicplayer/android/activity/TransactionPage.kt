package com.musicplayer.android.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.adapter.TransAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.TransactionPageBinding
import com.musicplayer.android.model.TransData

class TransactionPage : BaseActivity<TransactionPageBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.transaction_page
    }

    override fun initM() {
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setHistory()
    }

    private fun setHistory() {
        val list = mutableListOf<TransData>()
        list.add(
            TransData(
                1,
                title = "Signup Bonus",
                type = "Signup,",
                date = "4 May 2023",
                coins = "+10"
            )
        )
        list.add(
            TransData(
                2,
                title = "Daily Bonus",
                type = "Open App,",
                date = "4 May 2023",
                coins = "+7"
            )
        )
        list.add(
            TransData(
                3,
                title = "Complete Task",
                type = "Special Offer,",
                date = "4 May 2023",
                coins = "+7"
            )
        )
        list.add(
            TransData(
                4,
                title = "Paytm Redeem",
                type = "Redeem,",
                date = "6 May 2023",
                coins = "-100"
            )
        )

        binding.transRv.apply {
            adapter = TransAdapter(mActivity, list)
            layoutManager = LinearLayoutManager(mActivity)
        }
    }

}

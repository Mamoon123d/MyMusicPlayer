package com.musicplayer.android.activity

import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.InviteBinding

class Invite : BaseActivity<InviteBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.invite
    }

    override fun initM() {
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setInviteContent()
    }

    private fun setInviteContent() {
        //Glide.with(mActivity).load("https://cdn-icons-png.flaticon.com/512/2460/2460475.png").into(binding.headerImg)
        binding.steps.text = "Step 1: Click button to share the invite link\n\n" +
                "Step 2: Your friend take invitation,install App and Complete task\n\n" +
                "Step 3: Both of you get 100 free coins and you'll get friend's rebates\n\n"

        binding.totalCoinCon.setOnClickListener {
            goActivity(TransactionPage())
        }
    }

}

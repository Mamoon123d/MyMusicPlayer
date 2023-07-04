package com.musicplayer.android.offer

import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.RedeemSubmitBinding
import com.musicplayer.android.utils.Extension.Companion.gone

class RedeemSubmit : BaseActivity<RedeemSubmitBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.redeem_submit
    }

    override fun initM() {
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setRewardContent()
    }

    private fun setRewardContent() {
        Glide.with(mActivity)
            .load("https://play-lh.googleusercontent.com/k7yz57K2OxhNrPNKF2U18Zcv9rodOu7CfWh47U15FFUN8-_B0hQfXsM-BaLG0gOtvw=s180")
            .into(binding.rewardIv)
        binding.rewardTitle.text = "Paytm"

        binding.rsPlayerEt.gone()
        val payouts = arrayOf("200", "340", "500", "530", "600", "640")
        //val payoutsValue = arrayOf("200", "340", "500", "530", "600", "640")
        for (chipData in payouts) {
            val chip = this.layoutInflater.inflate(R.layout.chip_lay, null, false) as Chip
            chip.text = chipData
            binding.payChips.addView(chip)
            chip.setOnClickListener {
                if (chip.isChecked) {
                    binding.payValueTv.text = "${chipData} Cashback"

                }
            }

        }
        /*binding.payChips.setOnCheckedStateChangeListener { group, checkedIds ->
            binding.payChips.getChildAt()
        }*/
    }

}

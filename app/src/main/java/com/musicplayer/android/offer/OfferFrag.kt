package com.musicplayer.android.offer

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import com.musicplayer.android.R
import com.musicplayer.android.activity.Invite
import com.musicplayer.android.adapter.TaskAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.OfferFragBinding
import com.musicplayer.android.model.TaskData
import com.musicplayer.android.utils.AdKey

class OfferFrag : BaseFragment<OfferFragBinding>() {
    private lateinit var rewardedAd: MaxRewardedAd
    private var retryAttempt = 0.0

    override fun setLayoutId(): Int {
        return R.layout.offer_frag
    }

    override fun initM() {
        setOfferTask()
        //val i = lok { 98 }
    }

    fun <T> num(num: T): T {
        return num
    }

    inline fun <T> lok(body: () -> T): T {
        return body.invoke()
    }


    private fun setOfferTask() {
        val list = mutableListOf<TaskData>()
        list.add(
            TaskData(
                1,
                "Daily Bonus",
                "Receive daily bonus",
                "https://cdn-icons-png.flaticon.com/512/9562/9562261.png"
            )
        )
        list.add(
            TaskData(
                2,
                "Invite Friends",
                "Each friend earn 100 coins!\nLevel Up and get more easier to withdraw!",
                "https://cdn-icons-png.flaticon.com/512/1405/1405729.png"
            )
        )
        list.add(
            TaskData(
                3,
                "Special Offers",
                "Complete task and earn over 100000 coins!",
                "https://cdn-icons-png.flaticon.com/512/2743/2743307.png"
            )
        )
        list.add(
            TaskData(
                4,
                "Watch Videos",
                "Enjoy videos and get coin rewards",
                "https://cdn-icons-png.flaticon.com/512/3309/3309043.png"
            )
        )
        list.add(
            TaskData(
                5,
                "Redeem Reward",
                "Get more rewards and redeem in multiple reward",
                "https://cdn-icons-png.flaticon.com/512/1240/1240211.png"
            )
        )
        list.add(
            TaskData(
                6,
                "Subscribe us",
                "Subscribe and join us,Get coin rewards",
                "https://cdn-icons-png.flaticon.com/512/2111/2111748.png"
            )
        )

        binding.rvTask.apply {
            adapter = TaskAdapter(mActivity, list).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        when (list[position].id!!) {
                            //daily bonus
                            1 -> dailyTask()
                            //invite
                            2 -> goActivity(Invite())
                            //special Offers
                            3 -> goActivity(OfferList())
                            //watch video
                            4->watchVideo()
                            //Redeem Reward
                            5 -> goActivity(Rewards())

                        }
                    }
                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }


    }

    private fun watchVideo() {
      createRewardedAd()
    }

    private fun dailyTask() {
        createRewardedAd()
    }

    fun createRewardedAd() {
        rewardedAd = MaxRewardedAd.getInstance(AdKey.rewardedAdUnitId, mActivity)
        rewardedAd.setListener(object : MaxRewardedAdListener {
            override fun onAdLoaded(maxAd: MaxAd) {
                // Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'

                // Reset retry attempt
                retryAttempt = 0.0
                if (rewardedAd.isReady) {
                    rewardedAd.showAd()
                }
            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                // Rewarded ad failed to load
                // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                //retryAttempt++
                //  val delayMillis = TimeUnit.SECONDS.toMillis( Math.pow( 2.0, Math.min( 6.0, retryAttempt ) ).toLong() )

                //  Handler().postDelayed( { rewardedAd.loadAd() }, delayMillis )


            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                // Rewarded ad failed to display. We recommend loading the next ad
                rewardedAd.loadAd()
            }

            override fun onAdDisplayed(maxAd: MaxAd) {}

            override fun onAdClicked(maxAd: MaxAd) {}

            override fun onAdHidden(maxAd: MaxAd) {
                // rewarded ad is hidden. Pre-load the next ad
                // rewardedAd.loadAd()

            }

            override fun onRewardedVideoStarted(maxAd: MaxAd) {} // deprecated

            override fun onRewardedVideoCompleted(maxAd: MaxAd) {

            } // deprecated

            override fun onUserRewarded(maxAd: MaxAd, maxReward: MaxReward) {
                // Rewarded ad was displayed and user should receive the reward
            }
        })
        rewardedAd.loadAd()
    }

}
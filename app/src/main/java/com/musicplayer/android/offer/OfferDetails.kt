package com.musicplayer.android.offer

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.bumptech.glide.Glide
import com.musicplayer.android.R
import com.musicplayer.android.adapter.PayoutAdapter
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.OfferDetailsBinding
import com.musicplayer.android.model.PayoutStepData

class OfferDetails : BaseActivity<OfferDetailsBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.offer_details
    }

    override fun initM() {

        setContent()
    }

    private fun setContent() {
        // R.layout.pay_item
        Glide.with(mActivity)
            .load("https://play-lh.googleusercontent.com/pQa4sUQ64w_i-9ZNislOPmiXWGxlgna9tTivhXrMjJnAhW02892gvizv3ClOgOBG9MA")
            .into(binding.offerIv)
        binding.tvTitle.text = "Zupee App"
        binding.tvSubtitle.text = "upto 67 coins"
        binding.tvDes.text =
            "skill-based games that spark joy in the everyday lives of people by engaging and entertaining while they play."
        binding.tvLocDes.text =
            "Follow these steps to get reward:-\n (दिए गए निर्देशों का पालन करें और रिवॉर्ड पाएं) \n 1.Install the app.\n(ऐप इनस्टॉल करें)\n 2.Click on Register (रजिस्टर करें)\n3.Enter Mobile Number & Verify OTP(Compulsory)\n(मोबाइल नंबर डालें और OTP वेरीफाई करें)\nNote:-This Offer is only for Users who have not registered Earlier on this app.\n(यह ऑफर केवल उनके लिए है जिन्होंने इस ऐप पर पहले रजिस्टर न किया हो )\nnDisclaimer:-This game may be habit-forming or financially risky. Play responsibly.\n(इस गेम की लत लग सकती है और इसमें वित्तीय जोखिम शामिल है कृपया अपनी जिम्मेदारी पर खेलें)"

        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        payout()
        createMrecAd()
    }

    private fun payout() {
        val payouts = mutableListOf<PayoutStepData>()
        payouts.add(PayoutStepData("Install & Register The Zupee App", "80"))
        payouts.add(PayoutStepData("Install & Register The Zupee App", "80"))
        val lv = binding.payoutLv
        lv.adapter = PayoutAdapter(mActivity, payouts)

    }

    fun createMrecAd() {
        val adView = binding.mrAd
        val listener = object : MaxAdViewAdListener {
            override fun onAdLoaded(p0: MaxAd?) {
            }

            override fun onAdDisplayed(p0: MaxAd?) {
            }

            override fun onAdHidden(p0: MaxAd?) {
            }

            override fun onAdClicked(p0: MaxAd?) {
            }

            override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
            }

            override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
            }

            override fun onAdExpanded(p0: MaxAd?) {
            }

            override fun onAdCollapsed(p0: MaxAd?) {
            }


        }
        adView?.setListener(listener)

        // MREC width and height are 300 and 250 respectively, on phones and tablets
        /*  val widthPx = AppLovinSdkUtils.dpToPx(this, 300)
          val heightPx = AppLovinSdkUtils.dpToPx(this, 250)

          adView?.layoutParams = FrameLayout.LayoutParams(widthPx, heightPx)

          // Set background or background color for MRECs to be fully functional
         // adView?.setBackgroundColor(...)

          val rootView = findViewById<ViewGroup>(android.R.id.content)
          rootView.addView(adView)*/

        // Load the ad
        adView.loadAd()

        adView?.startAutoRefresh()
    }

}
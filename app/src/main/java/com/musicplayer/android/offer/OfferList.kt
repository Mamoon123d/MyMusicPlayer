package com.musicplayer.android.offer

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.OfferListBinding
import com.musicplayer.android.model.OfferAdapter
import com.musicplayer.android.model.OfferData

class OfferList : BaseActivity<OfferListBinding>() {

    override fun setLayoutId(): Int {
        return R.layout.offer_list
    }

    override fun initM() {
        setOffers()
        binding.backImg.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setOffers() {
        val list = mutableListOf<OfferData>()
        list.add(
            OfferData(
                "1",
                "Zupee App",
                "Install & Register",
                "https://sg-res.9appsdownloading.com/sg/res/jpg/49/bb/3a0c89f3565e8befeac144d66562-1xf.jpg?x-oss-process=style/mq200",
                "+40"
            )
        )
        list.add(
            OfferData(
                "1",
                "Dream 11",
                "Install & Register",
                "https://d13ir53smqqeyp.cloudfront.net/d11-static-pages/images/Dream11_Vertical_WhiteonRedBG.jpg",
                "+57"
            )
        )
        list.add(
            OfferData(
                "1",
                "Shaadi.com",
                "Install & Register",
                "https://cdn6.aptoide.com/imgs/d/5/b/d5bfa8eed492bbddf7b7fcaeb85ab7ff_icon.png",
                "+67"
            )
        )
        list.add(
            OfferData(
                "1",
                "Tata Neu App",
                "Install & Register",
                "https://byrajesh.com/wp-content/uploads/2022/12/TATA-NEU-Super-App.png",
                "+97"
            )
        )
        list.add(
            OfferData(
                "1",
                "Hippi App",
                "Install & Register",
                "https://is3-ssl.mzstatic.com/image/thumb/Purple116/v4/84/b5/2c/84b52c68-9cbe-79c5-87da-afb29cbd1fc2/AppIcon-0-0-1x_U007emarketing-0-0-0-7-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/512x512bb.jpg",
                "+70"
            )
        )


        OfferData(
            "1",
            "Hippi App",
            "Install & Register",
            "https://is3-ssl.mzstatic.com/image/thumb/Purple116/v4/84/b5/2c/84b52c68-9cbe-79c5-87da-afb29cbd1fc2/AppIcon-0-0-1x_U007emarketing-0-0-0-7-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/512x512bb.jpg",
            "+70"
        )


        binding.rvOffer.apply {
            adapter = OfferAdapter(mActivity, list).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        goActivity(OfferDetails())
                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }

}

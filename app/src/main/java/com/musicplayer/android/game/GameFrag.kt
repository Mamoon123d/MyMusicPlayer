package com.musicplayer.android.game

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.musicplayer.android.R
import com.musicplayer.android.adapter.GameAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.databinding.CategoryItemBinding
import com.musicplayer.android.databinding.CategoryLayBinding
import com.musicplayer.android.databinding.GameFragBinding
import com.musicplayer.android.databinding.GameLayBinding
import com.musicplayer.android.model.GCateData
import com.musicplayer.android.model.GameData
import com.musicplayer.android.network.NetworkHelper
import com.musicplayer.android.utils.MyIntent
import com.musicplayer.android.utils.view.OfflineDialog

class GameFrag : BaseFragment<GameFragBinding>() {

    companion object{
       public var categories=ArrayList<GCateData>()
        var list=ArrayList<GameData>()
    }
    override fun setLayoutId(): Int {
        return R.layout.game_frag
    }

    override fun initM() {
        setAllGames()
    }

    private fun setAllGames() {
         list = ArrayList()
        list.add(
            GameData(
                1,
                "Shooter",
                "https://browser.cdn.intl.miui.com/globalbrowser/content/debe26272fe08180ad0a1a76dd4dcc63"
            )
        )
        list.add(
            GameData(
                2,
                "Stack balls",
                "https://play-lh.googleusercontent.com/eveC_YZq8u4Mmu5-Ob4QY7e7ult-o4UCF1AEXVBjxFLf9vT8mM_F_P4u3lIOzfEp2rBj"
            )
        )
        list.add(
            GameData(
                3,
                "Bikes Rush",
                "https://mobimg.b-cdn.net/v2/fetch/00/0045f298bd0e470fda2ccd55c3516950.png"
            )
        )
        list.add(
            GameData(
                4,
                "Happy Farm",
                "https://media.tenor.com/LjK_JFTdI2UAAAAM/gala-town.gif"
            )
        )
        list.add(
            GameData(
                5,
                "Ludo Star",
                "https://lh3.googleusercontent.com/H-ERPjitHkOaLmPCzQITKLER4Xnc09onjvhBZvY20ibfE05d49vy2p_ea2ADz1DanB-b"
            )
        )
        setGameRow("Recommend", list)
        setGameRow("New Games", list)
         //categories = mutableListOf<GCateData>()
        categories=ArrayList()
        categories.add(
            GCateData(
                1,
                "Racing",
                "https://cdn-icons-png.flaticon.com/512/89/89102.png"
            )
        )
        categories.add(
            GCateData(
                2,
                "Shooting",
                "https://cdn-icons-png.flaticon.com/512/933/933942.png"
            )
        )
        categories.add(
            GCateData(
                2,
                "Arcade",
                "https://cdn-icons-png.flaticon.com/512/5045/5045939.png"
            )
        )
        categories.add(
            GCateData(
                2,
                "Classic",
                "https://cdn-icons-png.flaticon.com/512/7203/7203531.png"
            )
        )
        categories.add(
            GCateData(
                2,
                "Casual",
                "https://cdn-icons-png.flaticon.com/512/5891/5891400.png"
            )
        )
        setGameCateRow("Popular Genre")
        setGameRow("Racing", list)
    }

    private fun setGameCateRow(title: String) {
        val gameMain = binding.gameMain
        val cateLay = CategoryLayBinding.inflate(LayoutInflater.from(mActivity))
        cateLay.categoryTitle.text = title

        for (it in categories) {
            val chipBinding = CategoryItemBinding.inflate(LayoutInflater.from(mActivity))
            val chip = chipBinding.gameChip
            chip.id = it.id.toInt()
            chip.text = it.cateName
            chip.setOnClickListener {
                MyIntent.goGameCateIntent(mActivity,it.id)
               // goActivity(GameCategory())
            }
            Glide.with(mActivity).load(it.cateImg).into(object : CustomTarget<Drawable>(200, 200) {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    chip.chipIcon = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    chip.chipIcon = placeholder
                }

            })
            // chip.chipIcon=
            cateLay.cgGame.addView(chip)

        }
        gameMain.addView(cateLay.root)

    }

    private fun setGameRow(title: String, gameList: List<GameData>) {
        val gameMain = binding.gameMain
        val gameLay = GameLayBinding.inflate(LayoutInflater.from(mActivity))
        gameLay.categoryTitle.text = title
        gameLay.rvGame.apply {
            adapter = GameAdapter(mActivity, gameList).apply {
                setOnItemClickListener(object : BaseRvAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        val isNetwork = NetworkHelper.isNetworkAvailable(mActivity)
                        if (isNetwork) {
                              MyIntent.goGameIntent(mActivity,"https://games.cdn.famobi.com/html5games/s/smarty-bubbles-2/v200/?fg_domain=play.famobi.com&fg_aid=A1000-111&fg_uid=22d86193-c681-45c4-9f4d-8d5aab731603&fg_pid=e37ab3ce-88cd-4438-9b9c-a37df5d33736&fg_beat=385&original_ref=https%3A%2F%2Fplay.famobi.com%2Fsmarty-bubbles-2%2FA-FAMOBI-COM")
                        } else
                            OfflineDialog(mActivity).show()
                        //   this@GameFrag.showMsg("Network Not Available")
                    }

                })
            }
            layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
        }
        gameMain.addView(gameLay.root)
    }
}
package com.musicplayer.android.game

import com.google.android.material.tabs.TabLayoutMediator
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.base.MyPageAdapter
import com.musicplayer.android.databinding.GameCategoryBinding
import com.musicplayer.android.model.GCateData
import com.musicplayer.android.utils.MyIntent

class GameCategory : BaseActivity<GameCategoryBinding>() {
    lateinit var categories: ArrayList<GCateData>
    override fun setLayoutId(): Int {
        return R.layout.game_category
    }

    override fun initM() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setCateTabs()

    }

    private fun setCateTabs() {
        val cate_id = MyIntent.getGameCateId(mActivity)
        val vp = binding.cateVp
        this.categories = ArrayList()
        this.categories.addAll(GameFrag.categories)
        val tl = binding.categoryTabs
        val pageAdapter = MyPageAdapter(supportFragmentManager, lifecycle)
        for (it in this.categories) {
            val tab = tl.newTab().setText(it.cateName)
            tl.addTab(tab)
            pageAdapter.addFragment(GameListFrag(), it.cateName)
            if (it.id.toInt() == cate_id) {
                tl.selectTab(tab)
            }
        }

        vp.adapter = pageAdapter
        TabLayoutMediator(tl, vp) { tab, position ->
            tab.text = this.categories[position].cateName
        }.attach()
    }

}

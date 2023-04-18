package com.musicplayer.android.utils

import android.content.Context
import android.content.Intent
import com.musicplayer.android.activity.PlayerActivity

class MyIntent {
    companion object {
        const val CLASS: String = "class"
        fun goIntent(ctx: Context, pos: Int, ref: String) {
            PlayerActivity.position = pos
            val i = Intent(ctx, PlayerActivity::class.java)
            i.putExtra(CLASS, ref)
            ctx.startActivity(i)
        }
    }
}
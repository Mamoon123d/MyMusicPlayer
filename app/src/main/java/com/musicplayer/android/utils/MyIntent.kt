package com.musicplayer.android.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.musicplayer.android.activity.PlayerActivity
import com.musicplayer.android.game.GameCategory
import com.musicplayer.android.game.GameView
import com.musicplayer.android.music.MPlayerActivity
import com.musicplayer.android.offer.OfferDetails

class MyIntent {
    companion object {
        const val CLASS: String = "class"
        private const val OFFER_ID = "offerId"
        fun goIntent(ctx: Context, pos: Int, ref: String) {
            PlayerActivity.position = pos
            val i = Intent(ctx, PlayerActivity::class.java)
            i.putExtra(CLASS, ref)
            ctx.startActivity(i)
        }

        fun goMusicIntent(ctx: Context, pos: Int, ref: Int) {
            MPlayerActivity.songPosition = pos
            val i = Intent(ctx, MPlayerActivity::class.java)
            i.putExtra(CLASS, ref)
            ctx.startActivity(i)
        }

        fun goGameIntent(ctx: Context, gameUrl: String) {
            val i = Intent(ctx, GameView::class.java)
            i.putExtra("gameUrl", gameUrl)
            ctx.startActivity(i)
        }

        fun goGameCateIntent(ctx: Context, id: Int) {
            val i = Intent(ctx, GameCategory::class.java)
            i.putExtra("ref", id)
            ctx.startActivity(i)
        }

        fun goOfferIntent(ctx: Context, id: Int) {
            val i = Intent(ctx, OfferDetails::class.java)
            i.putExtra(OFFER_ID, id)
            ctx.startActivity(i)
        }

        fun getOfferId(ctx: Context): Int {
            return (ctx as Activity).intent.getIntExtra(OFFER_ID, -1)
        }

        fun getGameCateId(ctx: Context): Int? {
            return (ctx as Activity).intent.getIntExtra("ref", -1)
        }

        fun getGameUrl(ctx: Context): String? {
            return (ctx as Activity).intent.getStringExtra("gameUrl")
        }
    }
}
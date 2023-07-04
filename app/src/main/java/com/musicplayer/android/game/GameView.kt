package com.musicplayer.android.game

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.GameViewBinding
import com.musicplayer.android.utils.MyIntent


class GameView : BaseActivity<GameViewBinding>(isFullScreen = true) {

    override fun setLayoutId(): Int {
        return R.layout.game_view
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun initM() {
        val gameUrl = MyIntent.getGameUrl(mActivity)
        if (gameUrl != null) {
            val web = binding.web
            val setting = web.settings
            setting.javaScriptEnabled = true
            setting.domStorageEnabled = true
            web.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    view.loadUrl(url!!)
                    return true
                }

            }

            //  web.webViewClient = WebViewClient()
            web.webChromeClient = object:WebChromeClient(){

            }
            web.setOnKeyListener(object:OnKeyListener{
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    /*when(keyCode){
                       // KeyEvent.ACTION_DOWN
                    }*/

                    return false
                }

            })
            web.loadUrl(gameUrl)

        }
    }

}
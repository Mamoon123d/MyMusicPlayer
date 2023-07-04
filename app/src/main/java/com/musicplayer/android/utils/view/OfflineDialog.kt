package com.musicplayer.android.utils.view

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.musicplayer.android.databinding.OfflineDialogBinding


class OfflineDialog(val mContext: Context) : Dialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = OfflineDialogBinding.inflate(LayoutInflater.from(mContext))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window!!.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.goSetting.setOnClickListener {
            val i = Intent(Intent.ACTION_MAIN)
            i.component = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DataUsageSummaryActivity"
            )
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            mContext.startActivity(i)
            dismiss()
        }


    }

}
package com.musicplayer.android.activity

import android.os.Build
import android.os.Environment
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.SplashBinding
import com.musicplayer.android.utils.view.showAllMediaDailog

class Splash : BaseActivity<SplashBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.splash
    }

    override fun initM() {
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.isExternalStorageManager()
            } else {
                TODO("VERSION.SDK_INT < R")
                return
            }
        ) {
            //    showMsg("granted")
            // Permission granted. Now resume your workflow.
            goActivity(MainActivity())
        } else {
            // Permission is not granted
            showAllMediaDailog(mActivity, activityLauncher!!).apply {
                setOnPermissionListener(object : showAllMediaDailog.ObservePermission {
                    override fun permission(isGranted: Boolean) {
                        if (isGranted) {
                            updateUi()
                        }
                    }
                })
            }
        }
    }


    private fun updateUi() {
        goActivity(MainActivity())
    }


}
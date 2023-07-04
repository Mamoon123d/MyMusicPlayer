package com.musicplayer.android.utils.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResult
import com.musicplayer.android.BuildConfig
import com.musicplayer.android.base.BetterActivityResult


class showAllMediaDailog(mActivity:Context,activityLauncher : BetterActivityResult<Intent, ActivityResult>) {
  var observePermission:ObservePermission?=null
    init {
        val dialog = MyCustomDialog(
            mActivity,
            subtitle = "To find and play all media files on your device,we need the \"All files access\" permission on your device.",
            negativeButtonText = "Find standard media",
            positiveButtonText = "Find all",
            dialogStile = MyCustomDialog.STYLE_1
        ).apply {
            setOnDialogListener(object : MyCustomDialog.OnDialogListener {
                override fun onNegativeButton() {
                    val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    i.data = Uri.parse("package:" + context.packageName)
                    activityLauncher.launch(i){
                        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                Environment.isExternalStorageManager()
                            } else {
                                TODO("VERSION.SDK_INT < R")
                            }
                        ) {
                            dismiss()
                            //  updateUi()
                            //showMsg("granted")
                            observePermission!!.permission(true)
                            // Permission granted. Now resume your workflow.
                        }else{
                            observePermission!!.permission(false)
                        }
                    }
                    //context.startActivity(i)
                }

                override fun onPositiveButton() {
                    val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")

                    // startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
                    activityLauncher!!.launch(
                        Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                            uri
                        )
                    ) {
                        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                Environment.isExternalStorageManager()

                            } else {
                                TODO("VERSION.SDK_INT < R")
                            }
                        ) {
                            dismiss()
                          //  updateUi()
                            //showMsg("granted")
                            observePermission!!.permission(true)
                            // Permission granted. Now resume your workflow.
                        }else{
                            observePermission!!.permission(false)
                        }
                    }


                }

            })
        }
        dialog.show()
    }

    interface ObservePermission{
        fun permission(isGranted:Boolean)
    }
    fun setOnPermissionListener(observePermission: ObservePermission){
        this.observePermission=observePermission
    }
}
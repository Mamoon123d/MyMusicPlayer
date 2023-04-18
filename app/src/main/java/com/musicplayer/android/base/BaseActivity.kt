package com.musicplayer.android.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

public abstract class BaseActivity<D : ViewBinding>(val isFullScreen: Boolean? = false) :
    AppCompatActivity() {


    protected lateinit var mActivity: FragmentActivity
    protected lateinit var binding: D
    protected var tag = ""
    protected var activityLauncher: BetterActivityResult<Intent, ActivityResult>? = null


    //protected  var savedInstanceState: Bundle?=null
    protected open fun setExitAnimation(animId: Int) {
        overridePendingTransition(0, animId)
    }

    private fun setWindowFlag() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val params = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        window.attributes = params
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFullScreen!!) {
            setWindowFlag()
        }
        binding = DataBindingUtil.setContentView(this, setLayoutId())
        activityLauncher = BetterActivityResult.registerActivityForResult(this)
        // if (savedInstanceState!=null) this.savedInstanceState= savedInstanceState
        mActivity = this
        initM()

    }


    //logs...........
    protected open fun logD(msg: String, tag: String? = "") {
        Log.d("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logE(msg: String, tag: String? = "") {
        Log.e("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logI(msg: String, tag: String? = "") {
        Log.i("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logW(msg: String, tag: String? = "") {
        Log.w("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logV(msg: String, tag: String? = "") {
        Log.v("${this.localClassName} : $tag", "$msg : ")
    }
    //--------------------------------------------------------------


    protected open fun startForRegister(code_result: Int): ActivityResultLauncher<Intent> {

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                getResult(result, code_result)
                /* if (result.resultCode== Activity.RESULT_OK){
                   getResult(result,code_result)
                 }*/

            }
        return startForResult
    }

    protected open fun getResult(result: ActivityResult, code_result: Int) {}
    protected abstract fun initM()

    protected abstract fun setLayoutId(): Int

    protected open fun keepScreenOn() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    protected open fun goActivity(activityClass: Activity, bundle: Bundle? = null) {
        if (bundle != null)
            startActivity(Intent(this, activityClass::class.java).putExtras(bundle))
        else
            startActivity(Intent(this, activityClass::class.java))
    }

    protected open fun goActivity(
        activityClass: Activity,
        bundle: Bundle? = null,
        flag: Int? = null
    ) {
        if (bundle != null)
            startActivity(Intent(this, activityClass::class.java).putExtras(bundle))
        else
            startActivity(
                if (flag != null) Intent(
                    this,
                    activityClass::class.java
                ).setFlags(flag) else Intent(this, activityClass::class.java)
            )

    }

    protected open fun launchActivity(activityClass: Activity, code_result: Int) {
        startForRegister(code_result).launch(Intent(this, activityClass::class.java))
    }

    protected open fun goActivity(url: String) {
        if (url.isNotBlank()) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else showMsg("url is blank")
    }

    protected open fun showMsg(message: String) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
    }


    protected open fun setSystemBarColor(color: Int) {
        //ImmersionBar.with(this).statusBarColor(color)

        val window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        // clear FLAG_TRANSLUCENT_STATUS flag:

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, color)

    }

    protected open fun hideStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun Activity?.replaceFragment(
        @IdRes container_id: Int,
        fragment: Fragment,
        tag: String? = null,
        addToBackStack: Boolean = false
    ) {
        val compatActivity = this as? AppCompatActivity ?: return
        compatActivity.supportFragmentManager.beginTransaction().apply {
            replace(container_id, fragment, tag)
            if (addToBackStack) {
                addToBackStack(null)
            }
            commit()
        }
    }


}
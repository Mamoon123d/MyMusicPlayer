package com.musicplayer.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import com.applovin.sdk.AppLovinSdk
import com.musicplayer.android.base.BaseActivity
import com.musicplayer.android.databinding.ActivityMainBinding
import com.musicplayer.android.model.*
import com.musicplayer.android.music.MPlayerActivity
import com.musicplayer.android.utils.AdKey
import com.musicplayer.android.utils.view.MyCustomDialog
import com.musicplayer.android.utils.view.MyCustomDialog.Companion.STYLE_1

class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        lateinit var folderList: ArrayList<Folder2>
        lateinit var videoList: ArrayList<VideoMainData>
        lateinit var audioList: ArrayList<MainMusicData>
        lateinit var audioFolders: ArrayList<MusicFolder>
        lateinit var musicAlbumList: ArrayList<MusicAlbumData>
        lateinit var musicArtistList: ArrayList<MusicArtistData>
        var sortValue: Int = 0
        var sortOrder: Int = 0
        private val al_tag = "AppLovin:"

        val sortList = arrayOf(
            MediaStore.Video.Media.DATE_ADDED + " DESC",
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.TITLE + " DESC",
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.SIZE + " DESC"
        )

        val sortingList = arrayOf(
            MediaStore.Audio.Media.DATE_ADDED + " DESC", MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.SIZE + " DESC", MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
    }






    override fun initM() {
        setNav()

        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.isExternalStorageManager()
            } else {
                TODO("VERSION.SDK_INT < R")

            }
        ) {
            //    showMsg("granted")
            // Permission granted. Now resume your workflow.
            folderList = ArrayList()
            audioFolders = ArrayList()
            videoList = getAllVideos(mActivity)
            audioList = getAllMusic(mActivity)
            musicAlbumList = getAlbumList(mActivity)
            musicArtistList = getArtistList(mActivity)
        } else {
            folderList = ArrayList()
            videoList = ArrayList()
            audioList = ArrayList()
            audioFolders = ArrayList()
            musicAlbumList = ArrayList()
            musicArtistList = ArrayList()
            showAllMediaDialog()
        }
    }

    fun updateAlbumList(){
        musicAlbumList = ArrayList()
        musicAlbumList = getAlbumList(mActivity)
    }
    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    private fun showAllMediaDialog() {
        val dialog = MyCustomDialog(
            this@MainActivity,
            subtitle = "To find and play all media files on your device,we need the \"All files access\" permission on your device.",
            negativeButtonText = "Find standard media",
            positiveButtonText = "Find all",
            dialogStile = STYLE_1
        ).apply {
            setOnDialogListener(object : MyCustomDialog.OnDialogListener {
                override fun onNegativeButton() {
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
                            updateUi()
                            //showMsg("granted")
                            // Permission granted. Now resume your workflow.
                        }
                    }


                }

            })
        }
        dialog.show()
    }

    private fun updateUi() {

        folderList = ArrayList()
        audioFolders = ArrayList()
        videoList = getAllVideos(mActivity)
        audioList = getAllMusic(mActivity)
        musicAlbumList = getAlbumList(mActivity)
        musicArtistList = getArtistList(mActivity)
    }

    // Initialize the variable in before activity creation is complete.
    val storagePermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult?> {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Environment.isExternalStorageManager()

                } else {
                    TODO("VERSION.SDK_INT < R")
                }
            ) {
                showMsg("granted")
                // Permission granted. Now resume your workflow.
            }
        })

    private fun setNav() {
        val bn = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        // setupWithNavController(bn, navController)
        bn.setupWithNavController(navController)
    }

    /*private fun setVp() {
        val vp = binding.vp
        val bn = binding.bottomNav
        val adapter = VpAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(VideoFragment())
        adapter.addFragment(MusicFragment())
        adapter.addFragment(DiscoverFragment())
        adapter.addFragment(MeFragment())
        vp.apply {
            this.adapter = adapter
         *//*
        }
        *//*bn.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.video->{
                    vp.currentItem=0
                }
                R.id.music->{
                    vp.currentItem=1
                }
                R.id.discover->{
                    vp.currentItem=2
                }
                R.id.me->{
                    vp.currentItem=3
                }
            }
        true
        }*//*

        *//*vp.registerOnPageChangeCallback(object:OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0->bn.menu.findItem(R.id.video).isChecked=true
                    1->bn.menu.findItem(R.id.music).isChecked=true
                    2->bn.menu.findItem(R.id.discover).isChecked=true
                    3->bn.menu.findItem(R.id.me).isChecked=true
                }
            }
        })*//*
    }*/

    /* private fun requestRuntimePermission(): Boolean{
         if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
             return false
         }
         return true
     }

     override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<out String>,
         grantResults: IntArray
     ) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         if (requestCode==13){
             if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                 Toast.makeText(this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show()
             else
                 ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
         }
     }*/
    override fun onResume() {
        super.onResume()
        //for sorting
        val sortEditor = getSharedPreferences("SORTING", MODE_PRIVATE)
        val sortValue = sortEditor.getInt("sortOrder", 0)
        /* if(sortOrder!= sortValue){
         }*/
    }

    /* @SuppressLint("Recycle", "Range")
     @RequiresApi(Build.VERSION_CODES.O)
     private fun getAllVideo(): ArrayList<Video>{
         val tempList = ArrayList<Video>()
         val projection = arrayOf(
             MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE,
             MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA,
             MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.DURATION)
         val cursor = this.contentResolver.query(
             MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,null,null,
             MediaStore.Video.Media.DATE_ADDED + "DESC")
         if (cursor!=null)
             if (cursor.moveToNext())
                 do{
                     val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                     val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                     val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                     val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                     val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                     val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()
                     try {
                         val file = File(pathC)
                         val artUriC = Uri.fromFile(file)
                         val video = Video(title=titleC, id=idC, folderName = folderC,
                             duration = durationC, size=sizeC, path = pathC, artURi = artUriC
                         )
                         if (file.exists()) tempList.add(video)
                     }catch (_:java.lang.Exception){}
                 }while (cursor.moveToNext())
         cursor?.close()
         return tempList
     }
 */

    override fun onDestroy() {
        super.onDestroy()
        if (!MPlayerActivity.isPlaying && MPlayerActivity.musicService!=null){
            exitApplication()
        }
    }

    class MyAppOpenManager(applicationContext: Context?) : LifecycleObserver, MaxAdListener {
        private var appOpenAd: MaxAppOpenAd? = null
        private lateinit var context: Context

        init {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            context = applicationContext!!
            appOpenAd = MaxAppOpenAd(AdKey.appOpenAdUnitId, context)
            appOpenAd!!.setListener(this)
            appOpenAd!!.loadAd()
        }

        private fun showAdIfReady() {
            if (appOpenAd == null || !AppLovinSdk.getInstance(context).isInitialized) return

            if (appOpenAd!!.isReady) {
                //  Toast.makeText(context, "isReady=true", Toast.LENGTH_SHORT).show()
                appOpenAd!!.showAd(AdKey.appOpenAdUnitId)
            } else {
                appOpenAd!!.loadAd()
            }

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            showAdIfReady()
        }

        override fun onAdLoaded(ad: MaxAd) {
            Log.d(al_tag, "onAdLoaded: " + ad)
        }

        override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
            Log.d(al_tag, "onAdLoadFailed: " + error + "  adUnitId:${adUnitId}")
        }

        override fun onAdDisplayed(ad: MaxAd) {
            Log.d(al_tag, "onAdDisplay :" + ad)
        }

        override fun onAdClicked(ad: MaxAd) {
        }

        override fun onAdHidden(ad: MaxAd) {
            appOpenAd!!.loadAd()
        }

        override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
            appOpenAd!!.loadAd()
        }
    }

}
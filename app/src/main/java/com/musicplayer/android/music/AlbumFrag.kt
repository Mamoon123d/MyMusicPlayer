package com.musicplayer.android.music

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicplayer.android.MainActivity
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MusicAlbumAdapter
import com.musicplayer.android.base.BaseFragment
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.databinding.AlbumFragBinding
import com.musicplayer.android.model.MusicAlbumData
import com.musicplayer.android.utils.DataSet
import com.musicplayer.android.utils.DataSet.Reference.Companion.MUSIC_ALBUM


class AlbumFrag : BaseFragment<AlbumFragBinding>() {

    companion object {
        private lateinit var albumList: ArrayList<MusicAlbumData>
    }

    override fun setLayoutId(): Int {
        return R.layout.album_frag
    }

    override fun initM() {
        setAlbums()

    }

    private fun setAlbums() {
        albumList = MainActivity.musicAlbumList

        binding.albumRv.apply {
            adapter = MusicAlbumAdapter(mActivity, albumList).apply {
                setOnItemClickListener(object : BaseRvAdapter2.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        val data= albumList[position]
                        val b=Bundle()
                        b.putInt(DataSet.REF,MUSIC_ALBUM)
                        b.putString(DataSet.TITLE,data.albumName)
                        b.putString(DataSet.ALBUM_ID,data.albumId)
                        goActivity(MusicListHome(),b)
                    }
                })
            }
            layoutManager = LinearLayoutManager(mActivity)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val backStateName: String = fragment.javaClass.name
        val manager: FragmentManager = mActivity.supportFragmentManager
        val fragmentPopped: Boolean = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.replace(R.id.musicListCon, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    private fun loadFrag() {
        val transaction: FragmentTransaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.add(R.id.musicListCon, MusicListFrag())
        // transaction.addToBackStack(null)
        transaction.commit()
    }
}
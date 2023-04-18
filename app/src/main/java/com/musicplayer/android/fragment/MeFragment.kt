package com.musicplayer.android.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.musicplayer.android.R
import com.musicplayer.android.adapter.MeCategoryAdapter
import com.musicplayer.android.databinding.FragmentMeBinding
import com.musicplayer.android.model.MeCategoryData

class MeFragment : Fragment() {
    lateinit var binding: FragmentMeBinding
    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_me, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = ArrayList<MeCategoryData>()
       /* for (i in 1..20) {
            data.add(MeCategoryData( "Item $i", R.drawable.youtube_cover))
        }*/
        val adapter = MeCategoryAdapter(data,mActivity)
        val categoryRV=binding.rvMe
        categoryRV.adapter = adapter
        val layoutManager = GridLayoutManager(mActivity, 3)
        categoryRV.layoutManager = layoutManager
        val categoryRVAdapter = MeCategoryAdapter(data, mActivity)
        categoryRV.adapter =categoryRVAdapter

        // on below line we are adding data to our list
        data.add(MeCategoryData("Downloads", R.drawable.download))
        data.add(MeCategoryData("MP3 Converter", R.drawable.mp3))
        data.add(MeCategoryData("Privacy", R.drawable.feedback))
        data.add(MeCategoryData("History", R.drawable.history))
        data.add(MeCategoryData("Media Manage", R.drawable.rate_us))
      //  categoryRVAdapter.notifyDataSetChanged()
    }
}
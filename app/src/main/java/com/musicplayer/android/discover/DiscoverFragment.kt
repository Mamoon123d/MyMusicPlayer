package com.musicplayer.android.discover

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
import com.musicplayer.android.adapter.DiscoverAdapter
import com.musicplayer.android.databinding.FragmentDiscoverBinding
import com.musicplayer.android.model.DiscoverData

class DiscoverFragment : Fragment() {
    private lateinit var mActivity: FragmentActivity
    lateinit var binding: FragmentDiscoverBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity){
            mActivity=context
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = ArrayList<DiscoverData>()
        val adapter=DiscoverAdapter(mActivity,data)
        binding.discoverRecycler.adapter=adapter
        val layoutManager= GridLayoutManager(mActivity,4)
        binding.discoverRecycler.layoutManager=layoutManager
        data.add(DiscoverData(R.drawable.icons_40_whatsapp,"Whatsapp"))
        data.add(DiscoverData(R.drawable.facebook,"Facebook"))
        data.add(DiscoverData(R.drawable.instagram_logo,"Instagram"))
        data.add(DiscoverData(R.drawable.youtube,"Youtube"))
        data.add(DiscoverData(R.drawable.twitter,"Twitter"))
        data.add(DiscoverData(R.drawable.linkedin,"LinkedIn"))
        data.add(DiscoverData(R.drawable.telegram,"Telegram"))

    }
}
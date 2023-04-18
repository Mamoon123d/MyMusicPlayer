package com.musicplayer.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.musicplayer.android.R
import com.musicplayer.android.model.VideoData

class VideoHistoryAdapter(private val context: Context, private val mList: List<VideoData>):RecyclerView.Adapter<VideoHistoryAdapter.vh>() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.item_video_history,parent,false)
        return vh(view)
    }

    override fun onBindViewHolder(holder: vh, position: Int) {
    val list = mList[position]
       // Glide.with(context).load(list.img).into(holder.videoItem)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    class vh(ItemView:View) : RecyclerView.ViewHolder(ItemView) {
    val videoItem:ImageView=itemView.findViewById(R.id.ivYouTube_history)
    }
}
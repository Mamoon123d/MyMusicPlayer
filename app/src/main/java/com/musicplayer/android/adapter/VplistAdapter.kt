package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musicplayer.android.R
import com.musicplayer.android.model.VplistData

class VplistAdapter(val context: Context,val mlist: List<VplistData>):RecyclerView.Adapter<VplistAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(context).inflate(R.layout.video_playlist_view,parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val list=mlist[position]
        holder.folderName.text=list.title
        holder.fileCounter.text=list.fileCount

    }

    override fun getItemCount(): Int {
        return mlist.size
    }
    class viewHolder(ItemView: View) :RecyclerView.ViewHolder(ItemView){
        val videoItem: ImageView =itemView.findViewById(R.id.ivYouTube_playlist)
        val folderName: TextView = itemView.findViewById(R.id.folder_name)
        val fileCounter: TextView = itemView.findViewById(R.id.files_count)
    }
}
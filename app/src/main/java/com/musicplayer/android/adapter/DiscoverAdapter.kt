package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.musicplayer.android.R
import com.musicplayer.android.model.DiscoverData

class DiscoverAdapter(val context: Context, val mList: List<DiscoverData>): RecyclerView.Adapter<DiscoverAdapter.viewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverAdapter.viewholder {
        return viewholder(LayoutInflater.from(context).inflate(R.layout.discover_view,parent,false))
    }

    override fun onBindViewHolder(holder: DiscoverAdapter.viewholder, position: Int) {
        val list=mList[position]
        holder.socialIcon.setImageResource(list.icon)
        holder.socialName.text=list.title

    }
    override fun getItemCount(): Int {
        return mList.size
    }
    class viewholder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
    val socialIcon: ShapeableImageView=itemView.findViewById(R.id.iv_disc_icon)
    val socialName: TextView=itemView.findViewById(R.id.icon_name)
    }
}
package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musicplayer.android.R
import com.musicplayer.android.model.MeCategoryData

class MeCategoryAdapter(private val list: ArrayList<MeCategoryData>, private val context: Context) : RecyclerView.Adapter<MeCategoryAdapter.MeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return MeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeViewHolder, position: Int) {
        holder.nameTV.text = list[position].Name
        holder.categoryIV.setImageResource(list[position].Img)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    class MeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.idTVCategory)
        val categoryIV: ImageView = itemView.findViewById(R.id.idIVCategory)
    }
}
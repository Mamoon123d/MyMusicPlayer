package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.musicplayer.android.base.BaseRvAdapter2
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.ItemCategoryBinding
import com.musicplayer.android.model.MeCategoryData

class MeCategoryAdapter(context: Context, list: ArrayList<MeCategoryData>) : BaseRvAdapter2<MeCategoryData,MeCategoryAdapter.MyHolder>(context, list) {


    class MyHolder(val binding: ItemCategoryBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, t: MeCategoryData) {
            binding.idTVCategory.text=t.Name
            binding.idIVCategory.setImageResource(t.Img)
        }

    }



    override fun onBindData(holder: MyHolder, t: MeCategoryData) {
        holder.bind(context,t)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }



}
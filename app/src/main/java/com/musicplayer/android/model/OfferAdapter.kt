package com.musicplayer.android.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicplayer.android.base.BaseRvAdapter
import com.musicplayer.android.base.BaseRvViewHolder
import com.musicplayer.android.databinding.OfferItemBinding

class OfferAdapter(
    context: Context,
    list: List<OfferData>
) : BaseRvAdapter<OfferData, OfferAdapter.MyHolder>(context, list) {
    class MyHolder(val binding: OfferItemBinding) : BaseRvViewHolder(binding) {
        fun bind(context: Context, data: OfferData) {
            binding.tvTitle.text = data.offerName
            binding.tvSubtitle.text = data.offerDes
            binding.tvCoin.text = data.offerAmount
            Glide.with(context).load(data.offerImg).into(binding.ivOffer)
        }

    }

    override fun onBindData(holder: MyHolder, t: OfferData) {
        holder.bind(context, t)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            OfferItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

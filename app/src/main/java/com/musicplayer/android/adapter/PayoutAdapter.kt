package com.musicplayer.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.musicplayer.android.databinding.PayoutItemBinding
import com.musicplayer.android.model.PayoutStepData

class PayoutAdapter(val context: Context, val list: List<PayoutStepData>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): PayoutStepData {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //R.layout.pay_it
        val v = PayoutItemBinding.inflate(LayoutInflater.from(context), parent, false)
        //val v = LayoutInflater.from(context).inflate(R.layout.pay_item, parent, false)

        //val payoutTv = v.findViewById<TextView>(R.id.payout)
        //val payoutValue = v.findViewById<TextView>(R.id.payout_value)
        //val line = v.findViewById<View>(R.id.)
        val data = list[position]
        v.payout.text = data.propertyName
        v.payoutValue.text = data.propertyValue
        val line = v.payLine
        if (position == 0 && list.size > 1) {
            line.visibility = View.VISIBLE
        } else {
            line.visibility = View.GONE
        }
        return v.root
    }

}

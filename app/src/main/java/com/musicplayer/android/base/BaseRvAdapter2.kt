package com.musicplayer.android.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

public abstract class BaseRvAdapter2<T, VH : BaseRvViewHolder>(
    val context: Context,
    var list: MutableList<T>
) :
    RecyclerView.Adapter<VH>() {


    protected var onItemClickListener: OnItemClickListener? = null
    protected var onItemLongClickListener: OnItemLongClickListener? = null
    protected var itemPosition=-1

    // protected var list2:List<T>?=null

    override fun onBindViewHolder(holder: VH, position: Int) {
        //item click listener
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener { v ->
                onItemClickListener?.onItemClick(v, position)
            }
        }

        //set position of every item
        itemPosition=position
        /* if (onItemLongClickListener != null) {
             holder.itemView.setOnLongClickListener { v ->
                //dd
             }
         }*/

        onBindData(holder, list[position])
    }


    // abstract fun setLayoutItem(): Int
    protected abstract fun onBindData(holder: VH, t: T)

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }


    open fun addItem(data: T) {
        list.add(data)
        notifyDataSetChanged()
    }

    open fun addItemToPosition(data: T, position: Int) {
        list.toMutableList().add(position, data)
        notifyItemInserted(position)
    }


    open fun addAllItems(data: List<T>) {
        val oldCount = list.size
        list.addAll(data)
        notifyItemRangeInserted(oldCount, data.size)
    }

    open fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    open fun clearAllItems() {
        list.toMutableList().clear()
    }

    public interface OnItemClickListener {
        public fun onItemClick(v: View?, position: Int)
    }

    public interface OnItemLongClickListener {
        public fun onItemLongClick(v: View?, position: Int)
    }

    @JvmName("setOnItemClickListener1")
    public fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    @JvmName("setOnItemLongClickListener1")
    public fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener
    }


}
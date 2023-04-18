package com.musicplayer.android.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.musicplayer.android.R
import com.musicplayer.android.model.VideoFolderData
import com.musicplayer.android.video.FolderVideoActivity

class VideoFolderAdapter(private val context: Context, private val mList: List<VideoFolderData>):
    RecyclerView.Adapter<VideoFolderAdapter.vholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vholder {
        return vholder(LayoutInflater.from(context).inflate(R.layout.folder_view,parent,false))
    }

    override fun onBindViewHolder(holder: vholder, position: Int) {
        val list=mList[position]
        holder.folderName.text=list.folderName
        //Glide.with(holder.folderImg.context).load(list.artURi).apply(RequestOptions().placeholder(R.drawable.youtube_cover).centerCrop()).into(holder.thumbnailImageView)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, FolderVideoActivity::class.java)
            intent.putExtra("position", position)
            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class vholder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
        val folderImg: ImageView=itemView.findViewById(R.id.iv_folder)
        val folderName: TextView=itemView.findViewById(R.id.folder_name)
        val folderCount: TextView=itemView.findViewById(R.id.video_counter)
    }
}
package com.musicplayer.android.adapter

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.musicplayer.android.R
import com.musicplayer.android.databinding.VideoViewBinding
import com.musicplayer.android.model.VideoContentData
import com.musicplayer.android.video.VideoPlayActivity

class VideoSubAdapter(private val context: Context, private val videoList: List<VideoContentData>): RecyclerView.Adapter<VideoSubAdapter.vh>() {
    //private val onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        return vh(VideoViewBinding.inflate(LayoutInflater.from(context),parent,false))

    }
    override fun onBindViewHolder(holder: vh, position: Int) {
        val list = videoList[position]
        holder.title.text=list.title
        holder.date.text=list.date
        holder.duration.text= DateUtils.formatElapsedTime(list.duration/1000)
        Glide.with(holder.thumbnailImageView.context).load(list.artURi).apply(RequestOptions().placeholder(R.drawable.youtube_cover).centerCrop()).into(holder.thumbnailImageView)
      /*  try {
            val mediaPlayer: MediaPlayer = MediaPlayer.create(context, Uri.parse(list.path))
        }catch (e: Exception){
            e.printStackTrace()
        }*/
        holder.root.setOnClickListener{
            val intent = Intent(Intent(context, VideoPlayActivity::class.java))
            intent.putExtra("uri", list.artURi.toString())
            context.startActivity(intent)
          //  ContextCompat.startActivity(context,intent, null)

        }

    }

    /*private fun getDuration(duration: Int): String {
    val videoDuration: String
    val dur: Int=duration
    val hrs: Int=(dur/3600000)
    val min: Int=(dur/60000) % 6000
    val sec: Int=(dur % 60000 / 1000)
        if (hrs>0){
            videoDuration = String.format("%02d hrs, %02d min, %02d sec",hrs, min, sec)
        }else if (min>0)
        {
            videoDuration = String.format("%02d min, %02d sec", min, sec)
        }else{
            videoDuration = String.format("%02d sec", sec)
        }
return videoDuration
    }*/


    override fun getItemCount(): Int {
        return videoList.size
    }

    class vh(binding: VideoViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnailImageView: ImageView =itemView.findViewById(R.id.ivYouTube)
        val title=binding.videoName
        val duration=binding.videoDuration
        val date = binding.tvDate
        val root = binding.root
    }

}
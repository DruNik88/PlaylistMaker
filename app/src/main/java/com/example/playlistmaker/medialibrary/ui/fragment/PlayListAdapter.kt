package com.example.playlistmaker.medialibrary.ui.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import java.io.File

class PlayListAdapter() : RecyclerView.Adapter<PlayListViewHolder>() {

    private val playList: MutableList<PlayList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder =
        PlayListViewHolder(parent)

    override fun getItemCount(): Int {
        return playList.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearOrUpdatePlayList() {
        playList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun allUpdatePlayList(list: List<PlayList>) {
        playList.clear()
        playList.addAll(list)
        notifyDataSetChanged()
    }
}

class PlayListViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.playlist_view, parent, false)
) {

    private val image: ImageView = itemView.findViewById(R.id.imagePlaylist)
    private val title: TextView = itemView.findViewById(R.id.titlePlaylist)
    private val count: TextView = itemView.findViewById(R.id.countTracksPlaylist)

    fun bind(playList: PlayList) {
        val imageUri = playList.imageLocalStoragePath?.let { File(playList.imageLocalStoragePath) }
        val uri = imageUri?.takeIf { it.exists() }?.let { Uri.fromFile(imageUri) }
        Glide.with(itemView.context)
            .load(uri)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, itemView.context)))
            .into(image)
        title.text = playList.title
        count.text = trackEndings(playList.count)
    }


    companion object {
        const val RADIUS_IMAGE = 8.0F
    }
}

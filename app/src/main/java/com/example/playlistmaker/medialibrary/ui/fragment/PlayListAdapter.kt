package com.example.playlistmaker.medialibrary.ui.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.application.trackEndings
import com.example.playlistmaker.medialibrary.domain.model.PlayList
import java.io.File

class PlayListAdapter(
    private val onItemClickListener: ((PlayList) -> Unit)? = null
) : RecyclerView.Adapter<PlayListViewHolder>() {

    private val playList: MutableList<PlayList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder =
        PlayListViewHolder(parent)

    override fun getItemCount(): Int {
        return playList.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(playList[position])
        }
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
        val imageUri = playList.imageLocalStoragePath?.let { File(it) }
        val uri = imageUri?.takeIf { it.exists() }?.let { Uri.fromFile(it) }

        image.load(uri) {
            placeholder(R.drawable.ic_placeholder)
            transformations(
                RoundedCornersTransformation(
                    dpToPx(
                        RADIUS_IMAGE,
                        itemView.context
                    ).toFloat()
                )
            )
        }

        title.text = playList.title
        count.text = trackEndings(playList.count)
    }

    companion object {
        const val RADIUS_IMAGE = 8.0F // dp
    }
}

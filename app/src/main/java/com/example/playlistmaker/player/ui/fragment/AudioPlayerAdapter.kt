package com.example.playlistmaker.player.ui.fragment

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
import com.example.playlistmaker.player.domain.model.PlayListWithTrack
import com.example.playlistmaker.player.domain.model.PlayerList
import java.io.File

class AudioPlayerAdapter(
    private val onItemClickListener: ((PlayerList) -> Unit)? = null
) : RecyclerView.Adapter<AudioPlayerViewHolder>() {

    private val playerList: MutableList<PlayListWithTrack> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayerViewHolder =
        AudioPlayerViewHolder(parent)

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: AudioPlayerViewHolder, position: Int) {
        val playerList = playerList[position].playList
        holder.bind(playerList)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(playerList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearOrUpdatePlayerList() {
        playerList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun allUpdatePlayerList(list: List<PlayListWithTrack>) {
        playerList.clear()
        playerList.addAll(list)
        notifyDataSetChanged()
    }
}

class AudioPlayerViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.playlist_player_view, parent, false)
) {
    private val image: ImageView = itemView.findViewById(R.id.image_playlist)
    private val title: TextView = itemView.findViewById(R.id.title_playlist)
    private val countTrack: TextView = itemView.findViewById(R.id.count_tack_player_playlist)

    fun bind(playerList: PlayerList) {
        val imageUri =
            playerList.imageLocalStoragePath?.let { File(playerList.imageLocalStoragePath) }
        val uri = imageUri?.takeIf { it.exists() }?.let { Uri.fromFile(imageUri) }
        Glide.with(itemView.context)
            .load(uri)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, itemView.context)))
            .into(image)
        title.text = playerList.title
        countTrack.text = trackEndings(playerList.count)
    }

    companion object {
        const val RADIUS_IMAGE = 2.0F
    }


}

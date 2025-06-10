package com.example.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.medialibrary.domain.model.PlayList

import com.example.playlistmaker.player.domain.model.PlayerList

class AudioPlayerAdapter(): RecyclerView.Adapter<AudioPlayerViewHolder>() {

    val playerList: MutableList<PlayerList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayerViewHolder = AudioPlayerViewHolder(parent)

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: AudioPlayerViewHolder, position: Int) {
        holder.bind(playerList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearOrUpdatePlayerList(){
        playerList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun allUpdatePlayerList(list: List<PlayerList>){
        playerList.clear()
        playerList.addAll(list)
        notifyDataSetChanged()
    }
}

class AudioPlayerViewHolder(
    parent: ViewGroup
): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.playlist_player_view, parent,false)
){
    private val image: ImageView = itemView.findViewById(R.id.image_playlist)
    private val title: TextView = itemView.findViewById(R.id.title_playlist)
    private val countTrack: TextView = itemView.findViewById(R.id.count_tack_player_playlist)

    fun bind(playerList: PlayerList){
        val imageUri = playerList.imageInnerUri
        Glide.with(itemView.context)
            .load(imageUri)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, itemView.context)))
            .into(image)
        title.text = playerList.title
        countTrack.text = playerList.description
    }

    companion object {
        const val RADIUS_IMAGE = 2.0F
    }

}

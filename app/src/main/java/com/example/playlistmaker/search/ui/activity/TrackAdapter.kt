package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackList


class TrackAdapter(
    private val onItemClickListener: ((TrackSearchDomain) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder>() {

    val tracks: MutableList<TrackSearchDomain> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            onItemClickListener?.invoke(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size

    @SuppressLint("NotifyDataSetChanged")
    fun clearOrUpdateTracks(){
        tracks.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun allUpdateTracks(list: TrackList){
        tracks.clear()
        tracks.addAll(list.list)
        notifyDataSetChanged()
    }
}
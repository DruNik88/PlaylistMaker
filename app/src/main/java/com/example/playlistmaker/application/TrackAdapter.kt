package com.example.playlistmaker.application

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.model.TrackSearchDomain
import com.example.playlistmaker.search.domain.model.TrackSearchListDomain

class TrackAdapter<T: TrackGeneric>(
    private val onItemClickListener: ((T) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder<T>>() {

    val tracks: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder<T> =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder<T>, position: Int) {
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
    fun allUpdateTracks(list: List<T>){
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }
}

//
//class TrackAdapter(
//    private val onItemClickListener: ((TrackSearchDomain) -> Unit)? = null
//) : RecyclerView.Adapter<TrackViewHolder>() {
//
//    val tracks: MutableList<TrackSearchDomain> = mutableListOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
//        TrackViewHolder(parent)
//
//    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
//        holder.bind(tracks[position])
//        holder.itemView.setOnClickListener{
//            onItemClickListener?.invoke(tracks[position])
//        }
//    }
//
//    override fun getItemCount(): Int = tracks.size
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun clearOrUpdateTracks(){
//        tracks.clear()
//        notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun allUpdateTracks(list: TrackSearchListDomain){
//        tracks.clear()
//        tracks.addAll(list.list)
//        notifyDataSetChanged()
//    }
//}
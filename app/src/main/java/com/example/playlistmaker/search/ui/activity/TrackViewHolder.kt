package com.example.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.application.dpToPx
import com.example.playlistmaker.search.domain.model.TrackSearchDomain


class TrackViewHolder (
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_view, parent, false))
{
    private val sourceArtwork: ImageView = itemView.findViewById(R.id.artwork)
    private val sourceTrackName: TextView = itemView.findViewById(R.id.trackName)
    private val sourceArtistName: TextView = itemView.findViewById(R.id.artistName)
    private val sourceTrackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: TrackSearchDomain){
        val imageUrl = model.artworkUrl100
        Glide.with(itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, itemView.context)))
            .into(sourceArtwork)
        sourceTrackName.text= model.trackName
        sourceArtistName.text= model.artistName
        sourceTrackTime.text= model.trackTimeMillis
        sourceArtistName.requestLayout()
    }

    companion object{
        const val RADIUS_IMAGE = 2.0F
    }

}

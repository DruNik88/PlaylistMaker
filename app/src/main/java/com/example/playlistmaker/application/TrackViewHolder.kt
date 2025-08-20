package com.example.playlistmaker.application

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


class TrackViewHolder<T : TrackGeneric>(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_view, parent, false)
) {
    private val sourceArtwork: ImageView = itemView.findViewById(R.id.artwork)
    private val sourceTrackName: TextView = itemView.findViewById(R.id.trackName)
    private val sourceArtistName: TextView = itemView.findViewById(R.id.artistName)
    private val sourceTrackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: T) {
        // Установка изображения с помощью Coil
        sourceArtwork.load(model.artworkUrl100) {
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

        sourceTrackName.text = model.trackName
        sourceArtistName.text = model.artistName
        sourceTrackTime.text = model.trackTimeMillis
        sourceArtistName.requestLayout()
    }

    companion object {
        const val RADIUS_IMAGE = 2.0F // радиус в dp
    }
}
//class TrackViewHolder<T : TrackGeneric>(
//    parent: ViewGroup
//) : RecyclerView.ViewHolder(
//    LayoutInflater.from(parent.context)
//        .inflate(R.layout.track_view, parent, false)
//) {
//    private val sourceArtwork: ImageView = itemView.findViewById(R.id.artwork)
//    private val sourceTrackName: TextView = itemView.findViewById(R.id.trackName)
//    private val sourceArtistName: TextView = itemView.findViewById(R.id.artistName)
//    private val sourceTrackTime: TextView = itemView.findViewById(R.id.trackTime)
//
//    fun bind(model: T) {
//        val imageUrl = model.artworkUrl100
//        Glide.with(itemView.context)
//            .load(imageUrl)
//            .placeholder(R.drawable.ic_placeholder)
//            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, itemView.context)))
//            .into(sourceArtwork)
//        sourceTrackName.text = model.trackName
//        sourceArtistName.text = model.artistName
//        sourceTrackTime.text = model.trackTimeMillis
//        sourceArtistName.requestLayout()
//    }
//
//    companion object {
//        const val RADIUS_IMAGE = 2.0F
//    }
//
//}

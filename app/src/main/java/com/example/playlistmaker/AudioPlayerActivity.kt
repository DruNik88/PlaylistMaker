package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val RADIUS_IMAGE = 8.0F
        const val KEY_TRACK = "track"
    }

    private lateinit var trackNameApiAudioPlayer: TextView
    private lateinit var artistNameApiAudioPlayer: TextView
    private lateinit var trackTimeApiAudioPlayer: TextView
    private lateinit var collectionNameApiAudioPlayer: TextView
    private lateinit var releaseDateApiAudioPlayer: TextView
    private lateinit var primaryGenreNameApiAudioPlayer: TextView
    private lateinit var countryApiAudioPlayer: TextView
    private lateinit var collectionNameGroup: Group
    private lateinit var artworkApiAudioPlayer: ImageView
    private lateinit var playbackProgress: TextView

    private fun toolBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_audio_player)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.vector_arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun dataAudioPlayerActivity(track: Track) {
        val imageUrl = getCoverArtwork(track)
        Glide.with(applicationContext)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder_512)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, applicationContext)))
            .into(artworkApiAudioPlayer)
        trackNameApiAudioPlayer.text = track.trackName
        artistNameApiAudioPlayer.text = track.artistName
        playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackTimeApiAudioPlayer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        track.collectionName?.let{collectionNameApiAudioPlayer.text = it}
            ?: run{collectionNameGroup.visibility = View.GONE}
        track.releaseDate?.let{releaseDateApiAudioPlayer.text = it.substringBefore("-")}
            ?: run{getString(R.string.something_went_wrong)}
        track.primaryGenreName?.let{primaryGenreNameApiAudioPlayer.text = it}
            ?: run{primaryGenreNameApiAudioPlayer.text = getString(R.string.something_went_wrong)}
        track.country?.let{ countryApiAudioPlayer.text = it}
            ?: run{countryApiAudioPlayer.text = getString(R.string.something_went_wrong)}
    }

    private fun getCoverArtwork(track: Track) =
        track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track = intent.getParcelableExtra<Track>(KEY_TRACK)

        trackNameApiAudioPlayer = findViewById(R.id.track_name_api_audio_player)
        collectionNameGroup = findViewById(R.id.collection_name_group)
        artistNameApiAudioPlayer = findViewById(R.id.artist_name_api_audio_player)
        trackTimeApiAudioPlayer = findViewById(R.id.track_time_api_audio_player)
        releaseDateApiAudioPlayer = findViewById(R.id.release_date_api_audio_player)
        primaryGenreNameApiAudioPlayer = findViewById(R.id.primary_genre_name_api_audio_player)
        countryApiAudioPlayer = findViewById(R.id.country_api_audio_player)
        artworkApiAudioPlayer = findViewById(R.id.artwork_api_audio_player)
        collectionNameApiAudioPlayer = findViewById(R.id.collection_name_api_audio_player)
        playbackProgress = findViewById(R.id.playback_progress)

        toolBar()

        if (track != null) dataAudioPlayerActivity(track)

    }
}

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

    enum class TrackApi {
        TRACK_NAME,
        ARTIST_NAME,
        TRACK_TIME,
        ART_WORK_URL,
        COLLECTION_NAME,
        RELEASE_DATE,
        GENRE,
        COUNTRY
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
        trackNameApiAudioPlayer.text = audioPlayerApi(track, TrackApi.TRACK_NAME).toString()
        artistNameApiAudioPlayer.text = audioPlayerApi(track, TrackApi.ARTIST_NAME).toString()
        trackTimeApiAudioPlayer.text = audioPlayerApi(track, TrackApi.TRACK_TIME).toString()
        playbackProgress.text = audioPlayerApi(track, TrackApi.TRACK_TIME).toString()
        audioPlayerApi(track, TrackApi.RELEASE_DATE)
        audioPlayerApi(track, TrackApi.GENRE)
        audioPlayerApi(track, TrackApi.COUNTRY)
        audioPlayerApi(track, TrackApi.ART_WORK_URL)
        audioPlayerApi(track, TrackApi.COLLECTION_NAME)
    }

    private fun audioPlayerApi(track: Track, api: TrackApi) {
        when (api) {
            TrackApi.TRACK_NAME -> track.trackName
            TrackApi.ARTIST_NAME -> track.artistName
            TrackApi.TRACK_TIME -> SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            TrackApi.ART_WORK_URL -> {
                val imageUrl = getCoverArtwork(track)
                Glide.with(applicationContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder_512)
                    .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, applicationContext)))
                    .into(artworkApiAudioPlayer)
            }
            TrackApi.COLLECTION_NAME -> {
                if (track.collectionName.isNullOrEmpty()) {
                    collectionNameGroup.visibility = View.GONE
                } else {
                    collectionNameApiAudioPlayer.text = track.collectionName
                }
            }
            TrackApi.RELEASE_DATE -> {
                if (track.releaseDate.isNullOrEmpty()) {
                    releaseDateApiAudioPlayer.text = getString(R.string.something_went_wrong)
                } else {
                    releaseDateApiAudioPlayer.text = getYear(track)
                }
            }
            TrackApi.GENRE -> {
                if (track.primaryGenreName.isNullOrEmpty()) {
                    primaryGenreNameApiAudioPlayer.text = getString(R.string.something_went_wrong)
                } else {
                    primaryGenreNameApiAudioPlayer.text = track.primaryGenreName
                }
            }
            TrackApi.COUNTRY -> {
                if (track.country.isNullOrEmpty()) {
                    countryApiAudioPlayer.text = getString(R.string.something_went_wrong)
                } else {
                    countryApiAudioPlayer.text = track.country
                }
            }
        }
    }

    private fun getCoverArtwork(track: Track) =
        track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun getYear(track: Track) = track.releaseDate?.substringBefore("-")

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

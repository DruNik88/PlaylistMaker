package com.example.playlistmaker.application

import android.content.Context
import android.util.TypedValue
import com.example.playlistmaker.medialibrary.domain.model.TrackMediaLibraryDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}

fun trackEndings(count: Int): String {
    val remains100 = count % 100
    val remains10 = count % 10
    return if (remains100 in 11..19) {
        "$count треков"
    } else when (remains10) {
        1 -> "$count трек"
        2, 3, 4 -> "$count трека"
        else -> "$count треков"
    }
}

fun converterTime(track: TrackMediaLibraryDomain): Int {
    val trackTime = track.trackTimeMillis
    val listTime = trackTime?.split(":")
    val minuteToSeconds = listTime?.get(0)?.toInt()?.times(60) ?: 0
    val seconds = listTime?.get(1)?.toInt() ?: 0
    val durationTrackInSeconds = minuteToSeconds + seconds
    return durationTrackInSeconds
}

fun durationEndings(minutes: Int): String {
    val remain100 = minutes % 100
    val remain10 = minutes % 10
    return if (remain100 in 11..14) {
        "$minutes минут"
    } else when (remain10) {
        1 -> "$minutes минута"
        2, 3, 4 -> "$minutes минуты"
        else -> "$minutes минут"
    }
}

fun converterSecondsToMinutesAndEnding(time: Int): Int {
    return time / 60
}
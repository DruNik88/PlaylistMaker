package com.example.playlistmaker.application

import android.content.Context
import android.util.TypedValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()
}

fun <T> debounce(delayMillis: Long,
                 coroutineScope: CoroutineScope,
                 useLastParam: Boolean,
                 action: (T) -> Unit): (T) -> Unit {
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

fun trackEndings(count: Int): String{
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
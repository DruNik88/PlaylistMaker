package com.example.playlistmaker.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.application.isConnected

class InternetAccessibility: BroadcastReceiver() {
    @SuppressLint("RestrictedApi")
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == ConnectivityManager.CONNECTIVITY_ACTION){
            context?.let{
                if(!isConnected(context)){
                    Toast.makeText(context, context.getString(R.string.disabled_internet), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


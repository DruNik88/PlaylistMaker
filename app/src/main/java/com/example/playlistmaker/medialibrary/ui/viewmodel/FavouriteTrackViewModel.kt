package com.example.playlistmaker.medialibrary.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.interactor.FavouriteInteractor
import com.example.playlistmaker.medialibrary.domain.model.TrackFavourite
import com.example.playlistmaker.medialibrary.ui.state.FavouriteData
import kotlinx.coroutines.launch

class FavouriteTrackViewModel(
    private val favouriteInteractor: FavouriteInteractor
) : ViewModel() {

    private var favouriteLiveData = MutableLiveData<FavouriteData>()
    fun getFavouriteLiveData(): LiveData<FavouriteData> = favouriteLiveData

    init {
        viewModelScope.launch {
            favouriteInteractor.getTrackListInFavourite().collect { trackFavouriteList ->
                showFavouriteList(trackFavouriteList)
            }
        }
    }

    private fun showFavouriteList(trackFavouriteList: List<TrackFavourite>) {
        if (trackFavouriteList.isEmpty()) {
            favouriteLiveData.postValue(FavouriteData.Empty)
        } else {
            favouriteLiveData.postValue(FavouriteData.Content(trackFavouriteList))
        }
    }
}

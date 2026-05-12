package com.example.alcantarilla_trips.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcantarilla_trips.domain.TripImage
import com.example.alcantarilla_trips.domain.TripImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripImageViewModel @Inject constructor(
    private val repository: TripImageRepository
) : ViewModel() {

    private val _tripId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val images: StateFlow<List<TripImage>> = _tripId
        .filterNotNull()
        .flatMapLatest { repository.getImagesByTrip(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadImages(tripId: Int) { _tripId.value = tripId }

    fun addImage(tripId: Int, uriString: String) {
        viewModelScope.launch { repository.addImage(tripId, uriString) }
    }

    fun deleteImage(imageId: Int) {
        viewModelScope.launch { repository.deleteImage(imageId) }
    }
}
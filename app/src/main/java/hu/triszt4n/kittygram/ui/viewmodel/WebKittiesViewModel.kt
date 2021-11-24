package hu.triszt4n.kittygram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.database.KittygramDatabase
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.repository.CollectionRepository
import hu.triszt4n.kittygram.repository.KittyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebKittiesViewModel(application: Application): AndroidViewModel(application) {
    private val kittyRepository: KittyRepository
    private val collectionRepository: CollectionRepository
    init {
        val kittyDao = KittygramDatabase.getDatabase(application).kittyDao()
        val collectionDao = KittygramDatabase.getDatabase(application).collectionDao()
        kittyRepository = KittyRepository(kittyDao)
        collectionRepository = CollectionRepository(collectionDao, kittyDao)
    }

    val addedKittiesLiveData: MutableLiveData<MutableList<WebKitty>> = MutableLiveData()

    fun clearAddedKitties() {
        viewModelScope.launch {
            addedKittiesLiveData.value?.clear()
        }
    }

    fun initFirstPageKitties(tag: String? = null) {
        viewModelScope.launch {
            errorMessage = null
            val response = kittyRepository.getPaginatedWebKitties(tag, 1)
            if (response.isSuccessful) {
                addedKittiesLiveData.value = response.body()!!
            }
            else {
                errorMessage = response.errorBody().toString()
            }
        }
    }

    fun addAllKitties(tag: String? = null, page: Int) {
        viewModelScope.launch {
            errorMessage = null
            val response = kittyRepository.getPaginatedWebKitties(tag, page)
            if (response.isSuccessful) {
                addedKittiesLiveData.value = response.body()!!
            }
            else {
                errorMessage = response.errorBody().toString()
            }
        }
    }

    val collectionsLiveData: MutableLiveData<List<CollectionWithKitties>> = MutableLiveData()
    fun getAllCollections() {
        errorMessage = null
        viewModelScope.launch {
            collectionsLiveData.value = collectionRepository.getAllCollections()
        }
    }

    var errorMessage: String? = null
    fun addKittyToCollection(
        webKitty: WebKitty,
        collectionWithKitties: CollectionWithKitties,
        rating: Int,
        name: String
    ) {
        errorMessage = null
        if (name.length < 4) {
            errorMessage = "Name too short (under 4 characters)"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val kitty = Kitty(
                    webId = webKitty.id,
                    tags = webKitty.tags,
                    url = webKitty.url,
                    collectionId = collectionWithKitties.collection.id!!,
                    rating = rating,
                    name = name
            )
            errorMessage = if (!kittyRepository.addKitty(kitty)) "Couldn't create Kitty!" else null
        }
    }
}

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
import retrofit2.Response

class WebKittiesViewModel(application: Application): AndroidViewModel(application) {
    private val kittyRepository: KittyRepository
    private val collectionRepository: CollectionRepository
    init {
        val kittyDao = KittygramDatabase.getDatabase(application).kittyDao()
        val collectionDao = KittygramDatabase.getDatabase(application).collectionDao()
        kittyRepository = KittyRepository(kittyDao)
        collectionRepository = CollectionRepository(collectionDao, kittyDao)
    }

    val kittiesLiveData: MutableLiveData<Response<List<WebKitty>>> = MutableLiveData()
    fun getAllKitties(tag: String? = null, page: Int) {
        viewModelScope.launch {
            kittiesLiveData.value = kittyRepository.getAllWebKitties(tag, page)
        }
    }

    val collectionsLiveData: MutableLiveData<List<CollectionWithKitties>> = MutableLiveData()
    fun getAllCollections() {
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
        if (name.length < 4) {
            errorMessage = "Name too short (under 4 characters)"
            return
        }
        else {
            errorMessage = null
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

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

class WebKittiesViewModel(application: Application) : AndroidViewModel(application) {
    private val kittyRepository: KittyRepository
    private val collectionRepository: CollectionRepository

    init {
        val kittyDao = KittygramDatabase.getDatabase(application).kittyDao()
        val collectionDao = KittygramDatabase.getDatabase(application).collectionDao()
        kittyRepository = KittyRepository(kittyDao)
        collectionRepository = CollectionRepository(collectionDao, kittyDao)
    }

    var errorMessage: MutableLiveData<String?> = MutableLiveData()
    val webKitties: MutableLiveData<MutableList<WebKitty>> = MutableLiveData()
    val collections: MutableLiveData<List<CollectionWithKitties>> = MutableLiveData()
    val addedKitty: MutableLiveData<Kitty> = MutableLiveData()

    fun clearWebKitties() {
        viewModelScope.launch {
            webKitties.value?.clear()
        }
    }

    fun fetchMoreWebKitties(tag: String? = null, page: Int) {
        viewModelScope.launch {
            errorMessage.value = null
            val response = kittyRepository.getPaginatedWebKitties(tag, page)
            if (response.isSuccessful) {
                webKitties.value = response.body()!!
            } else {
                errorMessage.value = response.errorBody().toString()
            }
        }
    }

    fun getAllCollections() {
        errorMessage.value = null
        viewModelScope.launch {
            collections.value = collectionRepository.getAllCollections()
        }
    }

    fun addKittyToCollection(
        webKitty: WebKitty,
        collectionWithKitties: CollectionWithKitties?,
        rating: Int,
        name: String
    ) {
        errorMessage.value = null
        if (name.length < 4) {
            errorMessage.value = "Name too short (under 4 characters)"
            return
        }
        if (collectionWithKitties == null) {
            errorMessage.value = "No collection chosen!"
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
            val resultKitty: Kitty? = kittyRepository.addKitty(kitty)
            if (resultKitty != null) {
                addedKitty.postValue(resultKitty!!)
            } else {
                errorMessage.postValue("Kitty already in Collection!")
            }
        }
    }
}

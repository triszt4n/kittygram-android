package hu.triszt4n.kittygram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.database.KittygramDatabase
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.repository.CollectionRepository
import hu.triszt4n.kittygram.repository.KittyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionKittiesViewModel(application: Application): AndroidViewModel(application) {
    private val collectionRepository: CollectionRepository
    private val kittyRepository: KittyRepository

    init {
        val collectionDao = KittygramDatabase.getDatabase(application).collectionDao()
        val kittyDao = KittygramDatabase.getDatabase(application).kittyDao()
        collectionRepository = CollectionRepository(collectionDao, kittyDao)
        kittyRepository = KittyRepository(kittyDao)
    }

    var collectionId: Long? = null
    val collectionWithKitties: MutableLiveData<CollectionWithKitties> = MutableLiveData()
    var errorMessage: MutableLiveData<String?> = MutableLiveData()

    fun getCollection() {
        viewModelScope.launch {
            collectionWithKitties.value = collectionId?.let { collectionRepository.getCollectionWithKitties(it) }
        }
    }

    fun deleteCollection() {
        viewModelScope.launch {
            collectionWithKitties.value?.let { collectionRepository.deleteCollection(it) }
        }
    }

    fun deleteKitty(kitty: Kitty) {
        viewModelScope.launch(Dispatchers.IO) {
            kittyRepository.deleteKitty(kitty)
            getCollection()
        }
    }

    fun updateKitty(kitty: Kitty) {
        if (kitty.name.length < 4) {
            errorMessage.value = "Name too short (under 4 characters)"
            return
        }
        else {
            errorMessage.value = null
        }

        viewModelScope.launch(Dispatchers.IO) {
            kittyRepository.updateKitty(kitty)
            getCollection()
        }
    }
}
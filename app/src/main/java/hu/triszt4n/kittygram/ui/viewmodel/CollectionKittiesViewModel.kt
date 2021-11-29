package hu.triszt4n.kittygram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.database.KittygramDatabase
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.repository.CollectionRepository
import hu.triszt4n.kittygram.repository.KittyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CollectionKittiesViewModel(application: Application) : AndroidViewModel(application) {
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
    val errorMessage: MutableLiveData<String?> = MutableLiveData()

    fun getCollection() {
        errorMessage.value = null
        viewModelScope.launch {
            collectionWithKitties.value =
                collectionId?.let { collectionRepository.getCollectionWithKitties(it) }
        }
    }

    // Called only by IO coroutines
    private suspend fun postGetCollection() {
        collectionWithKitties.postValue(collectionRepository.getCollectionWithKitties(collectionId))
    }

    fun deleteCollection() {
        errorMessage.value = null
        runBlocking {
            collectionWithKitties.value?.let { collectionRepository.deleteCollection(it) }
        }
    }

    fun deleteKitty(kitty: Kitty) {
        errorMessage.value = null
        viewModelScope.launch(Dispatchers.IO) {
            kittyRepository.deleteKitty(kitty)
            postGetCollection()
        }
    }

    fun updateKitty(kitty: Kitty) {
        errorMessage.value = null
        if (kitty.name.length < 4) {
            errorMessage.value =
                getApplication<Application>().getString(R.string.warning_name_too_short)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            kittyRepository.updateKitty(kitty)
            postGetCollection()
        }
    }

    fun updateCollection(collection: CollectionWithKitties) {
        errorMessage.value = null
        if (collection.collection.name.length < 4) {
            errorMessage.value =
                getApplication<Application>().getString(R.string.warning_name_too_short)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.updateCollection(collection)
            postGetCollection()
        }
    }
}
package hu.triszt4n.kittygram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.database.KittygramDatabase
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CollectionRepository

    init {
        val collectionDao = KittygramDatabase.getDatabase(application).collectionDao()
        val kittyDao = KittygramDatabase.getDatabase(application).kittyDao()
        repository = CollectionRepository(collectionDao, kittyDao)
    }

    val collectionsWithKitties: MutableLiveData<List<CollectionWithKitties>> = MutableLiveData()
    var errorMessage: MutableLiveData<String?> = MutableLiveData()

    fun getAllCollections() {
        viewModelScope.launch {
            collectionsWithKitties.value = repository.getAllCollections()
        }
    }

    fun addCollection(name: String) {
        if (name.length < 4) {
            errorMessage.value = "Name too short (under 4 characters)"
            return
        } else {
            errorMessage.value = null
        }

        viewModelScope.launch(Dispatchers.IO) {
            val collection = Collection(name = name)
            repository.addCollection(collection)
            collectionsWithKitties.postValue(repository.getAllCollections())
        }
    }
}
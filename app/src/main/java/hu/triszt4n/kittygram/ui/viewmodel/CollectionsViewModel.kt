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

class CollectionsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: CollectionRepository
    init {
        val collectionDao = KittygramDatabase.getDatabase(application).collectionDao()
        val kittyDao = KittygramDatabase.getDatabase(application).kittyDao()
        repository = CollectionRepository(collectionDao, kittyDao)
    }

    val collectionsRes: MutableLiveData<List<CollectionWithKitties>> = MutableLiveData()
    fun getAllCollections() {
        viewModelScope.launch {
            collectionsRes.value = repository.getAllCollections()
        }
    }

    var errorMessage: String? = null
    fun addCollection(name: String) {
        if (name.length < 4) {
            errorMessage = "Name too short (under 4 characters)"
            return
        }
        else {
            errorMessage = null
        }

        viewModelScope.launch(Dispatchers.IO) {
            val collection = Collection(name = name)
            repository.addCollection(collection)
            collectionsRes.postValue(repository.getAllCollections())
        }
    }
}
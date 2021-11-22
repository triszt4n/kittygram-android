package hu.triszt4n.kittygram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.database.KittygramDatabase
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.repository.KittyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class WebKittiesViewModel(application: Application): AndroidViewModel(application) {
    private val repository: KittyRepository
    init {
        val kittyDao = KittygramDatabase.getDatabase(application).kittyDao()
        repository = KittyRepository(kittyDao)
    }

    val kittiesLiveData: MutableLiveData<Response<List<WebKitty>>> = MutableLiveData()
    fun getAllKitties(tag: String? = null) {
        viewModelScope.launch {
            kittiesLiveData.value = repository.getAllWebKitties(tag)
        }
    }

    var showAddError: Boolean = false
    fun addKittyToCollection(
        webKitty: WebKitty,
        collection: Collection,
        rating: Int,
        name: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val kitty = Kitty(
                    webId = webKitty.id,
                    tags = webKitty.tags,
                    url = webKitty.url,
                    collectionId = collection.id!!,
                    rating = rating,
                    name = name
            )
            showAddError = !repository.addKitty(kitty)
        }
    }
}

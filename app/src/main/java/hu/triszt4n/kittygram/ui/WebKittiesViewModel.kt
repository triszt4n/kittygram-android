package hu.triszt4n.kittygram.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.database.KittyDatabase
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.repository.KittyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class WebKittiesViewModel(application: Application): AndroidViewModel(application) {
    private val repository: KittyRepository
    init {
        val kittyDao = KittyDatabase.getDatabase(application).kittyDao()
        repository = KittyRepository(kittyDao)
    }

    val kittiesRes: MutableLiveData<Response<List<WebKitty>>> = MutableLiveData()
    fun getAllKitties(tag: String? = null) {
        viewModelScope.launch {
            kittiesRes.value = repository.getAllWebKitties(tag)
        }
    }

    var showAddError: Boolean = false
    fun addKittyToCollection(
        webKitty: WebKitty,
        collection: Collection,
        rating: Int? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val kitty = Kitty(webKitty, collection.id, rating)
            showAddError = !repository.addKitty(kitty)
        }
    }
}

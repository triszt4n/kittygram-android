package hu.triszt4n.kittygram

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.database.KittyDatabase
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.repository.KittyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: KittyRepository

    init {
        val kittyDao = KittyDatabase.getDatabase(application).kittyDao()
        repository = KittyRepository(kittyDao)
    }

    fun addKitty(kitty: Kitty) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addKitty(kitty)
        }
    }

    val myResponse: MutableLiveData<Response<WebKitty>> = MutableLiveData()

    fun getKitty() {
        viewModelScope.launch {
            val response = repository.getKitty("618ceada536db3001894b4f9")
            myResponse.value = response
        }
    }

}
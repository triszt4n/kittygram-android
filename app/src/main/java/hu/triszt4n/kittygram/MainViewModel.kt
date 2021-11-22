package hu.triszt4n.kittygram

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.triszt4n.kittygram.api.model.KittyJson
import hu.triszt4n.kittygram.repository.KittyRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: KittyRepository): ViewModel() {

    val myResponse: MutableLiveData<Response<KittyJson>> = MutableLiveData()

    fun getKitty() {
        viewModelScope.launch {
            val response = repository.getKitty("618ceada536db3001894b4f9")
            myResponse.value = response
        }
    }

}
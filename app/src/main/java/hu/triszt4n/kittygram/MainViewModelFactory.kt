package hu.triszt4n.kittygram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.triszt4n.kittygram.repository.KittyRepository

class MainViewModelFactory(private val repository: KittyRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}

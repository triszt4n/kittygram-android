package hu.triszt4n.kittygram.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KittygramViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass) {
            CollectionsViewModel::class.java -> CollectionsViewModel(application) as T
            WebKittiesViewModel::class.java -> WebKittiesViewModel(application) as T
            else -> throw IllegalArgumentException("No ViewModel found with this modelClass")
        }
    }

}

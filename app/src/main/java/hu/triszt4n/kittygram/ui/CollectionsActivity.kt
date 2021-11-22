package hu.triszt4n.kittygram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import hu.triszt4n.kittygram.databinding.ActivityCollectionsBinding
import hu.triszt4n.kittygram.ui.viewmodel.CollectionsViewModel
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory

class CollectionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionsBinding
    private lateinit var viewModel: CollectionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = KittygramViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CollectionsViewModel::class.java)
        viewModel.getAllCollections()
        viewModel.collectionsRes.observe(this, { collectionsWithKitties ->
            Log.d("COLLECTIONS HELLO", collectionsWithKitties.size.toString())
        })
    }
}
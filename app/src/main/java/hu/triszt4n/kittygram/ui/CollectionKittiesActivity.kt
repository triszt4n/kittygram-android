package hu.triszt4n.kittygram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.ActivityCollectionKittiesBinding
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.ui.adapter.CollectionKittyListAdapter
import hu.triszt4n.kittygram.ui.adapter.WebKittyListAdapter
import hu.triszt4n.kittygram.ui.viewmodel.CollectionKittiesViewModel
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory
import hu.triszt4n.kittygram.ui.viewmodel.WebKittiesViewModel

class CollectionKittiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionKittiesBinding
    private lateinit var viewModel: CollectionKittiesViewModel

    private val adapter: CollectionKittyListAdapter by lazy {
        CollectionKittyListAdapter(binding)
    }

    private val collectionId: Long by lazy {
        intent.extras?.get("collectionId") as Long
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionKittiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initRecyclerView()
        observeLiveData()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeLiveData() {
        val viewModelFactory = KittygramViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CollectionKittiesViewModel::class.java)
        viewModel.collectionId = collectionId
        viewModel.getCollection()
        viewModel.collectionRes.observe(this) { collectionWithKitties ->
            binding.toolbar.title = collectionWithKitties.collection.name
            adapter.loadItems(collectionWithKitties.kitties)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
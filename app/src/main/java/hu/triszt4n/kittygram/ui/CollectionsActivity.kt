package hu.triszt4n.kittygram.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.ActivityCollectionsBinding
import hu.triszt4n.kittygram.ui.adapter.CollectionListAdapter
import hu.triszt4n.kittygram.ui.adapter.WebKittyListAdapter
import hu.triszt4n.kittygram.ui.dialog.AddKittyDialog
import hu.triszt4n.kittygram.ui.dialog.CreateCollectionDialog
import hu.triszt4n.kittygram.ui.viewmodel.CollectionsViewModel
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory
import hu.triszt4n.kittygram.ui.viewmodel.WebKittiesViewModel
import hu.triszt4n.kittygram.util.MoshiInstance

class CollectionsActivity :
    CreateCollectionDialog.AddCollectionListener,
    CollectionListAdapter.CollectionOpenListener,
    AppCompatActivity()
{

    private lateinit var binding: ActivityCollectionsBinding
    private lateinit var viewModel: CollectionsViewModel
    private val adapter: CollectionListAdapter by lazy {
        CollectionListAdapter(binding, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        observeLiveData()

        binding.fab.setOnClickListener {
            CreateCollectionDialog(this)
                .show(supportFragmentManager, CreateCollectionDialog.TAG)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeLiveData() {
        val viewModelFactory = KittygramViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CollectionsViewModel::class.java)
        viewModel.getAllCollections()
        viewModel.collectionsRes.observe(this, { list ->
            adapter.loadItems(list)
        })
    }

    override fun onSaveCollection(name: String) {
        viewModel.addCollection(name)
        if (viewModel.errorMessage != null) {
            Snackbar
                .make(binding.root, viewModel.errorMessage!!, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onOpenCollection(collection: CollectionWithKitties) {
        val intent = Intent(this, CollectionKittiesActivity::class.java)
        intent.putExtra("collectionId", collection.collection.id)
        startActivity(intent)
    }
}

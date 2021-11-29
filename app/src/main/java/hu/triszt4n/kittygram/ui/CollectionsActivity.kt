package hu.triszt4n.kittygram.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.ActivityCollectionsBinding
import hu.triszt4n.kittygram.ui.adapter.CollectionListAdapter
import hu.triszt4n.kittygram.ui.dialog.CreateCollectionDialog
import hu.triszt4n.kittygram.ui.viewmodel.CollectionsViewModel
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory

class CollectionsActivity :
    CreateCollectionDialog.AddCollectionListener,
    CollectionListAdapter.CollectionOpenListener,
    AppCompatActivity() {

    private lateinit var binding: ActivityCollectionsBinding
    private lateinit var viewModel: CollectionsViewModel
    private val adapter: CollectionListAdapter by lazy {
        CollectionListAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initRecyclerView()
        observeLiveData()

        binding.toolbar.title = getString(R.string.collections_title)

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

        viewModel.collectionsWithKitties.observe(this, { list ->
            adapter.loadItems(list)
        })

        viewModel.errorMessage.observe(this) { msg ->
            if (msg == null)
                return@observe

            Snackbar
                .make(binding.root, msg, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onSaveCollection(name: String) {
        viewModel.addCollection(name)
    }

    override fun onOpenCollection(collection: CollectionWithKitties) {
        val intent = Intent(this, CollectionKittiesActivity::class.java).apply {
            putExtra("collectionId", collection.collection.id)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCollections()
    }
}

package hu.triszt4n.kittygram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.ui.adapter.WebKittyListAdapter
import hu.triszt4n.kittygram.ui.dialog.AddKittyDialog
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory
import hu.triszt4n.kittygram.ui.viewmodel.WebKittiesViewModel

class WebKittiesActivity :
    AppCompatActivity(),
    WebKittyListAdapter.WebKittySaveClickListener,
    AddKittyDialog.AddKittyListener
{
    private lateinit var binding: ActivityWebKittiesBinding
    private lateinit var viewModel: WebKittiesViewModel

    private var page: Int = 1
    private var collections: List<CollectionWithKitties> = emptyList()

    private val adapter: WebKittyListAdapter by lazy {
        WebKittyListAdapter(binding, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebKittiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initRecyclerView()
        observeLiveData()

        binding.nestedScrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                ++page
                viewModel.getAllKitties(page = page)
            }
        }

        binding.fab.setOnClickListener {
            binding.nestedScrollView.fullScroll(View.FOCUS_UP)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeLiveData() {
        val viewModelFactory = KittygramViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WebKittiesViewModel::class.java)
        viewModel.getAllKitties(page = page)
        viewModel.getAllCollections()
        viewModel.kittiesLiveData.observe(this, { response ->
            if (response.isSuccessful) {
                response.body()?.let { adapter.loadItems(it) }
            }
            else {
                Snackbar.make(
                        binding.root,
                        response.errorBody().toString(),
                        Snackbar.LENGTH_LONG
                ).show()
            }
        })
        viewModel.collectionsLiveData.observe(this, { list ->
            collections = list
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onItemSaved(item: WebKitty) {
        AddKittyDialog(this)
            .addKitty(item)
            .addCollections(collections)
            .show(supportFragmentManager, AddKittyDialog.TAG)
    }

    override fun onSaveKitty(webKitty: WebKitty, name: String, rating: Int, collection: CollectionWithKitties) {
        viewModel.addKittyToCollection(webKitty, collection, rating, name)
        if (viewModel.errorMessage != null) {
            Snackbar
                .make(binding.root, viewModel.errorMessage!!, Snackbar.LENGTH_LONG)
                .show()
        }
    }
}
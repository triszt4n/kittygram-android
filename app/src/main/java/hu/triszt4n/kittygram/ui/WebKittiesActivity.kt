package hu.triszt4n.kittygram.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.MainActivity
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
    AddKittyDialog.AddKittyListener {
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

        binding.recyclerView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                binding.progressBar.visibility = View.VISIBLE
                ++page
                viewModel.fetchMoreWebKitties(page = page)
            }
        }

        binding.fab.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeLiveData() {
        val viewModelFactory = KittygramViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WebKittiesViewModel::class.java)

        binding.progressBar.visibility = View.VISIBLE
        viewModel.fetchMoreWebKitties(page = 1)
        viewModel.getAllCollections()

        viewModel.webKitties.observe(this) { kitties ->
            binding.progressBar.visibility = View.GONE
            Log.d("KITTIES LOADED IN", "size: ${kitties?.size}: $kitties")
            adapter.addItems(kitties)
        }

        viewModel.collections.observe(this) { collections ->
            this.collections = collections
        }

        viewModel.addedKitty.observe(this) { kitty ->
            if (kitty != null) {
                val intent = Intent(this, CollectionKittiesActivity::class.java).apply {
                    putExtra("collectionId", kitty.collectionId)
                }
                startActivity(intent)
            }
        }

        viewModel.errorMessage.observe(this) { msg ->
            if (msg != null) {
                Snackbar
                    .make(binding.root, msg, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onItemSaved(item: WebKitty) {
        AddKittyDialog(this)
            .addKitty(item)
            .addCollections(collections)
            .show(supportFragmentManager, AddKittyDialog.TAG)
    }

    override fun onSaveKitty(
        webKitty: WebKitty,
        name: String,
        rating: Int,
        collection: CollectionWithKitties
    ) {
        viewModel.addKittyToCollection(webKitty, collection, rating, name)
    }

    override fun onBackPressed() {
        val backIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        startActivity(backIntent)
    }
}
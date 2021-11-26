package hu.triszt4n.kittygram.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.MainActivity
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.repository.KittyRepository.Companion.PAGING_LIMIT
import hu.triszt4n.kittygram.ui.adapter.WebKittyListAdapter
import hu.triszt4n.kittygram.ui.dialog.AddKittyDialog
import hu.triszt4n.kittygram.ui.dialog.GotoKittyDialog
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory
import hu.triszt4n.kittygram.ui.viewmodel.WebKittiesViewModel

class WebKittiesActivity :
    AppCompatActivity(),
    WebKittyListAdapter.WebKittySaveClickListener,
    GotoKittyDialog.GotoKittyListener,
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
            binding.kittyCountContainer.visibility = View.VISIBLE
            binding.kittyCount.text = "${page * PAGING_LIMIT} Kitties loaded"
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

    // Boilerplate code
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val toolbarMenu: Menu = binding.toolbar.menu
        menuInflater.inflate(R.menu.webkitties_menu_toolbar, toolbarMenu)
        for (i in 0 until toolbarMenu.size()) {
            val menuItem: MenuItem = toolbarMenu.getItem(i)
            menuItem.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
            if (menuItem.hasSubMenu()) {
                val subMenu: SubMenu = menuItem.subMenu
                for (j in 0 until subMenu.size()) {
                    subMenu.getItem(j)
                        .setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.goto_position_button -> {
                GotoKittyDialog(this)
                    .show(supportFragmentManager, GotoKittyDialog.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    override fun onGotoKitty(position: Int) {
        if (position in (1 .. page * PAGING_LIMIT)) {
            binding.recyclerView.scrollToPosition(position - 1)
        }
        else {
            Snackbar
                .make(binding.root, "Invalid position entered!", Snackbar.LENGTH_LONG)
                .show()
        }
    }
}
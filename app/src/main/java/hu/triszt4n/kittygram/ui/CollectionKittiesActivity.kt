package hu.triszt4n.kittygram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.ActivityCollectionKittiesBinding
import hu.triszt4n.kittygram.ui.adapter.CollectionKittyListAdapter
import hu.triszt4n.kittygram.ui.dialog.UpdateKittyDialog
import hu.triszt4n.kittygram.ui.viewmodel.CollectionKittiesViewModel
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory

class CollectionKittiesActivity :
    CollectionKittyListAdapter.CollectionKittyListener,
    UpdateKittyDialog.UpdateKittyListener,
    AppCompatActivity() {

    private lateinit var binding: ActivityCollectionKittiesBinding
    private lateinit var viewModel: CollectionKittiesViewModel

    private val adapter: CollectionKittyListAdapter by lazy {
        CollectionKittyListAdapter(binding, this)
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
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CollectionKittiesViewModel::class.java)

        viewModel.collectionId = collectionId
        viewModel.getCollection()

        viewModel.collectionWithKitties.observe(this) { collectionWithKitties ->
            binding.toolbar.title = collectionWithKitties.collection.name
            adapter.loadItems(collectionWithKitties.kitties)
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
        menuInflater.inflate(R.menu.menu_toolbar, toolbarMenu)
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
            R.id.collection_delete_button -> {
                AlertDialog.Builder(binding.root.context)
                    .setTitle("Delete Collection")
                    .setMessage("Are you sure you want to delete the entire collection?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteCollection()
                        this.finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDeleteClicked(kitty: Kitty) {
        AlertDialog.Builder(binding.root.context)
            .setTitle("Delete Kitty from Collection")
            .setMessage("Are you sure you want to remove ${kitty.name} from collection?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteKitty(kitty)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onUpdateClicked(kitty: Kitty) {
        UpdateKittyDialog(this)
            .addKitty(kitty)
            .show(supportFragmentManager, UpdateKittyDialog.TAG)
    }

    override fun onSaveKitty(kitty: Kitty) {
        viewModel.updateKitty(kitty)
    }
}
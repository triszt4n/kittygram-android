package hu.triszt4n.kittygram.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.ActivityCollectionKittiesBinding
import hu.triszt4n.kittygram.ui.adapter.CollectionKittyListAdapter
import hu.triszt4n.kittygram.ui.dialog.UpdateCollectionDialog
import hu.triszt4n.kittygram.ui.dialog.UpdateKittyDialog
import hu.triszt4n.kittygram.ui.viewmodel.CollectionKittiesViewModel
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory

class CollectionKittiesActivity :
    CollectionKittyListAdapter.CollectionKittyListener,
    UpdateKittyDialog.UpdateKittyListener,
    UpdateCollectionDialog.UpdateCollectionListener,
    AppCompatActivity() {

    private lateinit var binding: ActivityCollectionKittiesBinding
    private lateinit var viewModel: CollectionKittiesViewModel

    private val adapter: CollectionKittyListAdapter by lazy {
        CollectionKittyListAdapter(binding, this)
    }

    private val collectionId: Long by lazy {
        intent.extras?.get("collectionId") as Long
    }

    private var collectionWithKitties: CollectionWithKitties? = null

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
            val count = collectionWithKitties.kitties.size
            if (count > 0) {
                binding.collectionCountContainer.visibility = View.VISIBLE
                binding.collectionCount.text =
                    getString(R.string.kitties_numbered, count.toString())
            } else {
                binding.collectionCountContainer.visibility = View.GONE
            }

            this.collectionWithKitties = collectionWithKitties
            adapter.loadItems(collectionWithKitties.kitties)
        }

        viewModel.errorMessage.observe(this) { msg ->
            if (msg == null)
                return@observe

            Snackbar
                .make(binding.root, msg, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    // Boilerplate code
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val toolbarMenu: Menu = binding.toolbar.menu
        menuInflater.inflate(R.menu.collection_menu_toolbar, toolbarMenu)
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
                    .setTitle(getString(R.string.prompt_delete_collection))
                    .setMessage(getString(R.string.prompt_delete_collection_question))
                    .setPositiveButton(getString(R.string.delete)) { _, _ ->
                        viewModel.deleteCollection()
                        this.finish()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
                true
            }
            R.id.collection_update_button -> {
                if (collectionWithKitties == null) {
                    Snackbar
                        .make(
                            binding.root,
                            getString(R.string.warning_no_collection_loaded),
                            Snackbar.LENGTH_LONG
                        )
                        .show()
                    false
                } else {
                    UpdateCollectionDialog(this)
                        .addCollection(collectionWithKitties!!)
                        .show(supportFragmentManager, UpdateCollectionDialog.TAG)
                    true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDeleteClicked(kitty: Kitty) {
        AlertDialog.Builder(binding.root.context)
            .setTitle(getString(R.string.prompt_delete_kitty_from_collection))
            .setMessage(
                getString(
                    R.string.prompt_delete_kitty_from_collection_question,
                    kitty.name
                )
            )
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteKitty(kitty)
            }
            .setNegativeButton(getString(R.string.cancel), null)
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

    override fun onSaveCollection(collection: CollectionWithKitties) {
        viewModel.updateCollection(collection)
    }
}
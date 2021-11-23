package hu.triszt4n.kittygram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.databinding.ActivityCollectionKittiesBinding
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.ui.adapter.CollectionKittyListAdapter
import hu.triszt4n.kittygram.ui.adapter.WebKittyListAdapter
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory
import hu.triszt4n.kittygram.ui.viewmodel.WebKittiesViewModel

class CollectionKittiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionKittiesBinding
    private lateinit var viewModel: WebKittiesViewModel

    private var page: Int = 1

    private val adapter: CollectionKittyListAdapter by lazy {
        CollectionKittyListAdapter(binding)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionKittiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setSupportActionBar(binding.toolbar)

        initRecyclerView()
        observeLiveData()

        // binding.fab.setOnClickListener {
            // binding.nestedScrollView.fullScroll(View.FOCUS_UP)
        // }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeLiveData() {
        val viewModelFactory = KittygramViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WebKittiesViewModel::class.java)
        viewModel.getAllKitties(page = page)
        viewModel.kittiesLiveData.observe(this, { response ->
            Log.d("KITTIES HELLO", response.body()?.size.toString())
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
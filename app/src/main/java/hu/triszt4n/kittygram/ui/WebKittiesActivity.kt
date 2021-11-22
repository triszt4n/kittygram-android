package hu.triszt4n.kittygram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.ui.adapter.WebKittyListAdapter
import hu.triszt4n.kittygram.ui.viewmodel.KittygramViewModelFactory
import hu.triszt4n.kittygram.ui.viewmodel.WebKittiesViewModel

class WebKittiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebKittiesBinding
    private lateinit var viewModel: WebKittiesViewModel

    private val adapter: WebKittyListAdapter by lazy {
        WebKittyListAdapter(binding)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebKittiesBinding.inflate(layoutInflater)
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
        viewModel = ViewModelProvider(this, viewModelFactory).get(WebKittiesViewModel::class.java)
        viewModel.getAllKitties()
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
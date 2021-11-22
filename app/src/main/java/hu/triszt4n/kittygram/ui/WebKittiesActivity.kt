package hu.triszt4n.kittygram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding

class WebKittiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebKittiesBinding
    private lateinit var viewModel: WebKittiesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebKittiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = WebKittiesViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WebKittiesViewModel::class.java)
        viewModel.getAllKitties()
        viewModel.kittiesRes.observe(this, { kitties ->
            Log.d("KITTIES HELLO", kitties.body()?.size.toString())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
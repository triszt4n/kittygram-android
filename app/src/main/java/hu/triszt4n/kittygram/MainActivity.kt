package hu.triszt4n.kittygram

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import hu.triszt4n.kittygram.databinding.ActivityMainBinding
import hu.triszt4n.kittygram.ui.CollectionsActivity
import hu.triszt4n.kittygram.ui.WebKittiesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("kittygram_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean("quittingApplication", false)
            apply()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kittiesCard.setOnClickListener {
            val intent = Intent(this, WebKittiesActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            startActivity(intent)
        }

        binding.collectionsCard.setOnClickListener {
            val intent = Intent(this, CollectionsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPreferences = getSharedPreferences("kittygram_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean("quittingApplication", true)
            apply()
        }
    }
}

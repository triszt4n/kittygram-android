package hu.triszt4n.kittygram.ui.adapter

import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.databinding.ListRowWebKittyBinding
import hu.triszt4n.kittygram.util.Constants
import retrofit2.Response

class WebKittyListAdapter(
        private val binding: ActivityWebKittiesBinding
) :
    RecyclerView.Adapter<WebKittyListAdapter.WebKittyViewHolder>() {

    private var items = emptyList<WebKitty>()

    fun loadItems(kitties: List<WebKitty>) {
        items = kitties
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WebKittyViewHolder(
            ListRowWebKittyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: WebKittyViewHolder, position: Int) {
        val kitty = items[position]

        Glide.with(binding.root.context)
                .load("${Constants.BASE_URL}/${kitty.url}")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.kittyImage)

        holder.binding.kittyImage.setOnClickListener {
            // TODO: implement ImageViewer
            Snackbar.make(
                    binding.root,
                    "You've clicked on the image!",
                    Snackbar.LENGTH_LONG
            ).show()
        }

        holder.binding.kittySaveButton.setOnClickListener {
            // TODO: Open up SaveDialogFragment
            Snackbar.make(
                    binding.root,
                    "You've clicked on the button!",
                    Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun getItemCount(): Int = items.size

    inner class WebKittyViewHolder(val binding: ListRowWebKittyBinding) : RecyclerView.ViewHolder(binding.root)
}

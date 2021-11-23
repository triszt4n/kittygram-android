package hu.triszt4n.kittygram.ui.adapter

import android.annotation.SuppressLint
import android.media.Image
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.stfalcon.imageviewer.StfalconImageViewer
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.databinding.ListRowWebKittyBinding
import hu.triszt4n.kittygram.util.Constants
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WebKittyListAdapter(
        private val binding: ActivityWebKittiesBinding,
        private val listener: WebKittySaveClickListener
) :
    RecyclerView.Adapter<WebKittyListAdapter.WebKittyViewHolder>() {

    interface WebKittySaveClickListener {
        fun onItemSaved(item: WebKitty)
    }

    private var items = emptyList<WebKitty>()
    private var stfalconImageViewer: StfalconImageViewer.Builder<WebKitty>? = null

    fun loadItems(kitties: List<WebKitty>) {
        items = kitties

        stfalconImageViewer = StfalconImageViewer.Builder(binding.root.context, items) { view, image ->
            Glide
                .with(binding.root.context)
                .load("${Constants.BASE_URL}/${image.url}")
                .into(view)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WebKittyViewHolder(
            ListRowWebKittyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WebKittyViewHolder, position: Int) {
        val kitty = items[position]

        Glide.with(binding.root.context)
                .load("${Constants.BASE_URL}/${kitty.url}")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.kittyImage)

        holder.binding.kittyImage.setOnClickListener {
            stfalconImageViewer
                ?.withStartPosition(position)
                ?.show(true)
        }

        holder.binding.kittyTags.text = kitty.tags.let { if (it.isEmpty()) "" else it.toString() }
        holder.binding.kittyDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(kitty.createdAt)

        holder.binding.kittySaveButton.setOnClickListener {
            listener.onItemSaved(kitty)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class WebKittyViewHolder(val binding: ListRowWebKittyBinding) : RecyclerView.ViewHolder(binding.root)
}

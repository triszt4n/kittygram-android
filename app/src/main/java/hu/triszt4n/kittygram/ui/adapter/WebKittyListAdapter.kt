package hu.triszt4n.kittygram.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.databinding.ListRowWebKittyBinding
import hu.triszt4n.kittygram.util.Constants
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

    private var items = mutableListOf<WebKitty>()

    private val stfalconImageViewerBuilder by lazy {
        StfalconImageViewer
            .Builder(binding.root.context, items) { view, image ->
                Glide
                    .with(binding.root.context)
                    .load("${Constants.BASE_URL}/${image.url}")
                    .into(view)
            }
    }

    fun addItems(kitties: List<WebKitty>) {
        val oldLastPosition = items.size
        items.addAll(kitties)
        notifyItemRangeInserted(oldLastPosition, kitties.size)
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
            .into(holder.binding.kittyImage)

        holder.binding.apply {
            kittyImage.setOnClickListener { view ->
                stfalconImageViewerBuilder
                    .withStartPosition(position)
                    .withTransitionFrom(view as ImageView?)
                    .show()
            }

            kittySaveButton.setOnClickListener {
                listener.onItemSaved(kitty)
            }

            kittyPosition.text = "#${position + 1}"
            kittyTags.text = kitty.tags.let { if (it.isEmpty()) "" else it.toString() }
            kittyDate.text =
                SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(kitty.createdAt)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class WebKittyViewHolder(val binding: ListRowWebKittyBinding) :
        RecyclerView.ViewHolder(binding.root)
}

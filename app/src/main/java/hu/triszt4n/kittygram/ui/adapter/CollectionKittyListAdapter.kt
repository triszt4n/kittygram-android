package hu.triszt4n.kittygram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.stfalcon.imageviewer.StfalconImageViewer
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.ActivityCollectionKittiesBinding
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.databinding.ListRowCollectionKittyBinding
import hu.triszt4n.kittygram.databinding.ListRowWebKittyBinding
import hu.triszt4n.kittygram.util.Constants

class CollectionKittyListAdapter(
        private val binding: ActivityCollectionKittiesBinding,
        private val listener: CollectionKittyListener
) :
    RecyclerView.Adapter<CollectionKittyListAdapter.CollectionKittyViewHolder>() {

    interface CollectionKittyListener {
        fun onDeleteClicked(kitty: Kitty)
        fun onUpdateClicked(kitty: Kitty)
    }

    private var items = emptyList<Kitty>()
    private var stfalconImageViewer: StfalconImageViewer.Builder<Kitty>? = null

    fun loadItems(kitties: List<Kitty>) {
        items = kitties

        stfalconImageViewer = StfalconImageViewer.Builder(binding.root.context, items) { view, image ->
            Glide
                .with(binding.root.context)
                .load("${Constants.BASE_URL}/${image.url}")
                .into(view)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CollectionKittyViewHolder(
            ListRowCollectionKittyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CollectionKittyViewHolder, position: Int) {
        val kitty = items[position]

        Glide.with(binding.root.context)
                .load("${Constants.BASE_URL}/${kitty.url}")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.collectionKittyImage)

        holder.binding.kittyName.text = kitty.name
        holder.binding.kittyRating.rating = kitty.rating?.toFloat() ?: 0.0f
        holder.binding.kittyTags.text = kitty.tags.toString()

        holder.binding.collectionKittyImage.setOnClickListener {
            stfalconImageViewer
                ?.withStartPosition(position)
                ?.show(true)
        }

        holder.binding.kittyDeleteButton.setOnClickListener {
            listener.onDeleteClicked(kitty)
        }

        holder.binding.kittyModifyButton.setOnClickListener {
            listener.onUpdateClicked(kitty)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class CollectionKittyViewHolder(val binding: ListRowCollectionKittyBinding) : RecyclerView.ViewHolder(binding.root)
}

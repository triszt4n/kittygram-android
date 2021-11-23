package hu.triszt4n.kittygram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.ActivityCollectionKittiesBinding
import hu.triszt4n.kittygram.databinding.ActivityWebKittiesBinding
import hu.triszt4n.kittygram.databinding.ListRowCollectionKittyBinding
import hu.triszt4n.kittygram.databinding.ListRowWebKittyBinding
import hu.triszt4n.kittygram.util.Constants

class CollectionKittyListAdapter(
        private val binding: ActivityCollectionKittiesBinding
) :
    RecyclerView.Adapter<CollectionKittyListAdapter.CollectionKittyViewHolder>() {

    private var items = emptyList<Kitty>()

    fun loadItems(kitties: List<Kitty>) {
        items = kitties
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

        holder.binding.collectionKittyImage.setOnClickListener {
            // TODO: implement ImageViewer
            Snackbar.make(
                    binding.root,
                    "You've clicked on the image!",
                    Snackbar.LENGTH_LONG
            ).show()
        }

        holder.binding.kittyDeleteButton.setOnClickListener {
            // TODO: Open up SaveDialogFragment
            Snackbar.make(
                    binding.root,
                    "You've clicked on the delete button!",
                    Snackbar.LENGTH_LONG
            ).show()
        }

        holder.binding.kittyModifyButton.setOnClickListener {
            // TODO: Open up SaveDialogFragment
            Snackbar.make(
                    binding.root,
                    "You've clicked on the modify button!",
                    Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun getItemCount(): Int = items.size

    inner class CollectionKittyViewHolder(val binding: ListRowCollectionKittyBinding) : RecyclerView.ViewHolder(binding.root)
}

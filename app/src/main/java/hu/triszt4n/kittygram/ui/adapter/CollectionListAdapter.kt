package hu.triszt4n.kittygram.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.ActivityCollectionsBinding
import hu.triszt4n.kittygram.databinding.ListRowCollectionBinding

class CollectionListAdapter(
    private val listener: CollectionOpenListener
) : RecyclerView.Adapter<CollectionListAdapter.CollectionViewHolder>() {
    private var items = emptyList<CollectionWithKitties>()

    interface CollectionOpenListener {
        fun onOpenCollection(collection: CollectionWithKitties)
    }

    fun loadItems(collections: List<CollectionWithKitties>) {
        items = collections
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CollectionViewHolder(
        ListRowCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collectionWithKitties = items[position]
        val context = holder.binding.root.context

        holder.binding.apply {
            collectionName.text = collectionWithKitties.collection.name
            collectionCount.text =
                if (collectionWithKitties.kitties.isEmpty()) context.getString(R.string.empty_collection_label)
                else context.getString(
                    R.string.itemcounter_numbered,
                    collectionWithKitties.kitties.size.toString()
                )

            collectionOpenButton.setOnClickListener {
                listener.onOpenCollection(collectionWithKitties)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class CollectionViewHolder(val binding: ListRowCollectionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
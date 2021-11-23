package hu.triszt4n.kittygram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.ActivityCollectionsBinding
import hu.triszt4n.kittygram.databinding.ListRowCollectionBinding

class CollectionListAdapter(
        private val binding: ActivityCollectionsBinding
) :
        RecyclerView.Adapter<CollectionListAdapter.CollectionViewHolder>() {

    private var items = emptyList<CollectionWithKitties>()

    fun loadItems(collections: List<CollectionWithKitties>) {
        items = collections
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CollectionViewHolder(
            ListRowCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collectionWithKitties = items[position]

        holder.binding.collectionName.text = collectionWithKitties.collection.name

        holder.binding.collectionOpenButton.setOnClickListener {
            // TODO: implement ImageViewer
            Snackbar.make(
                    binding.root,
                    "You've clicked on the open button!",
                    Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun getItemCount(): Int = items.size

    inner class CollectionViewHolder(val binding: ListRowCollectionBinding) : RecyclerView.ViewHolder(binding.root)
}
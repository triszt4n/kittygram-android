package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.DialogCreateCollectionBinding
import hu.triszt4n.kittygram.databinding.DialogUpdateCollectionBinding

class UpdateCollectionDialog(
    private val listener: UpdateCollectionListener
) : DialogFragment() {

    companion object {
        const val TAG = "UpdateCollectionDialog"
    }

    interface UpdateCollectionListener {
        fun onSaveCollection(collection: CollectionWithKitties)
    }

    private lateinit var binding: DialogUpdateCollectionBinding
    private lateinit var collection: CollectionWithKitties

    fun addCollection(collection: CollectionWithKitties): UpdateCollectionDialog {
        this.collection = collection
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUpdateCollectionBinding.inflate(layoutInflater)

        binding.collectionName.apply {
            setText(collection.collection.name)
            addTextChangedListener { edit ->
                if (edit.toString().length < 4) {
                    binding.collectionName.error = "Name too short (>3 characters)"
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Update Collection")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                collection.collection.name = binding.collectionName.text.toString()
                listener.onSaveCollection(collection)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
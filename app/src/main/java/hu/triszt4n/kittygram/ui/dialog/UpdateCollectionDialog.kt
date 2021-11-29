package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.DialogCreateOrUpdateCollectionBinding

class UpdateCollectionDialog(
    private val listener: UpdateCollectionListener
) : DialogFragment() {

    companion object {
        const val TAG = "UpdateCollectionDialog"
    }

    interface UpdateCollectionListener {
        fun onSaveCollection(collection: CollectionWithKitties)
    }

    private lateinit var binding: DialogCreateOrUpdateCollectionBinding
    private lateinit var collection: CollectionWithKitties

    fun addCollection(collection: CollectionWithKitties): UpdateCollectionDialog {
        this.collection = collection
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCreateOrUpdateCollectionBinding.inflate(layoutInflater)

        binding.collectionName.apply {
            setText(collection.collection.name)
            addTextChangedListener { edit ->
                if (edit.toString().length < 4) {
                    binding.collectionName.error = getString(R.string.warning_name_too_short)
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.prompt_update_collection))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                collection.collection.name = binding.collectionName.text.toString()
                listener.onSaveCollection(collection)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
}
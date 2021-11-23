package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.databinding.DialogCreateCollectionBinding

class CreateCollectionDialog(
    private val listener: AddCollectionListener
) : DialogFragment() {

    companion object {
        const val TAG = "AddCollectionDialog"
    }

    interface AddCollectionListener {
        fun onSaveCollection(name: String)
    }

    private lateinit var binding: DialogCreateCollectionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCreateCollectionBinding.inflate(layoutInflater)
        binding.collectionName.addTextChangedListener { edit ->
            if (edit.toString().length < 4) {
                binding.collectionName.error = "Name too short (>3 characters)"
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Create Collection")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                listener.onSaveCollection(binding.collectionName.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
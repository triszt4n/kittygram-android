package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.databinding.DialogCreateOrUpdateCollectionBinding

class CreateCollectionDialog(
    private val listener: AddCollectionListener
) : DialogFragment() {

    companion object {
        const val TAG = "AddCollectionDialog"
    }

    interface AddCollectionListener {
        fun onSaveCollection(name: String)
    }

    private lateinit var binding: DialogCreateOrUpdateCollectionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCreateOrUpdateCollectionBinding.inflate(layoutInflater)
        binding.collectionName.addTextChangedListener { edit ->
            if (edit.toString().length < 4) {
                binding.collectionName.error = getString(R.string.warning_name_too_short)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.prompt_create_collection))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                listener.onSaveCollection(binding.collectionName.text.toString())
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
}
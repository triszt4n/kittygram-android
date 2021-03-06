package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable

import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.DialogAddOrUpdateKittyBinding

class AddKittyDialog(
    private val listener: AddKittyListener
) : DialogFragment() {

    companion object {
        const val TAG = "AddKittyDialog"
    }

    interface AddKittyListener {
        fun onSaveKitty(webKitty: WebKitty, name: String, rating: Int, collection: CollectionWithKitties?)
    }

    private lateinit var binding: DialogAddOrUpdateKittyBinding
    private lateinit var collections: List<CollectionWithKitties>
    private lateinit var kitty: WebKitty

    fun addCollections(collections: List<CollectionWithKitties>): AddKittyDialog {
        this.collections = collections
        return this
    }

    fun addKitty(kitty: WebKitty): AddKittyDialog {
        this.kitty = kitty
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddOrUpdateKittyBinding.inflate(layoutInflater)

        binding.collectionsSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            collections.map {
                it.collection.name
            }
        )

        binding.kittyName.addTextChangedListener { text: Editable? ->
            if (text.toString().length < 4) {
                binding.kittyName.error = getString(R.string.warning_name_too_short)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.prompt_add_kitty))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                listener.onSaveKitty(
                    kitty,
                    binding.kittyName.text.toString(),
                    binding.kittyRating.rating.toInt(),
                    collections[binding.collectionsSpinner.selectedItemPosition]
                )
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
}
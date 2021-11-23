package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable

import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.databinding.DialogAddKittyBinding

class AddKittyDialog(
    private val listener: AddKittyListener
) : DialogFragment() {

    companion object {
        const val TAG = "AddKittyDialog"
    }

    interface AddKittyListener {
        fun onSaveKitty(webKitty: WebKitty, name: String, rating: Int, collection: CollectionWithKitties)
    }

    private lateinit var binding: DialogAddKittyBinding
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
        binding = DialogAddKittyBinding.inflate(layoutInflater)

        binding.collectionsSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            collections.map {
                it.collection.name
            }
        )

        binding.kittyName.addTextChangedListener { text: Editable? ->
            if (text.toString().length < 4) {
                binding.kittyName.error = "Name too short (>3 characters)"
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Kitty to Collection")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                listener.onSaveKitty(
                    kitty,
                    binding.kittyName.text.toString(),
                    binding.kittyRating.numStars,
                    collections.get(binding.collectionsSpinner.selectedItemPosition)
                )
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
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
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.DialogAddKittyBinding
import hu.triszt4n.kittygram.databinding.DialogUpdateKittyBinding

class UpdateKittyDialog(
    private val listener: UpdateKittyListener
) : DialogFragment() {

    companion object {
        const val TAG = "UpdateKittyDialog"
    }

    interface UpdateKittyListener {
        fun onSaveKitty(kitty: Kitty)
    }

    private lateinit var binding: DialogUpdateKittyBinding
    private lateinit var kitty: Kitty

    fun addKitty(kitty: Kitty): UpdateKittyDialog {
        this.kitty = kitty
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUpdateKittyBinding.inflate(layoutInflater)

        binding.kittyName.apply {
            setText(kitty.name)
            addTextChangedListener { text: Editable? ->
                if (text.toString().length < 4) {
                    binding.kittyName.error = "Name too short (>3 characters)"
                }
            }
        }

        binding.kittyRating.rating = kitty.rating?.toFloat() ?: 0.0f

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Kitty to Collection")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                kitty.name = binding.kittyName.text.toString()
                kitty.rating = binding.kittyRating.rating.toInt()
                listener.onSaveKitty(kitty)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
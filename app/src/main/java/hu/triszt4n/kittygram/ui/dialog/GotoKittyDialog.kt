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
import hu.triszt4n.kittygram.databinding.DialogGotoKittyBinding

class GotoKittyDialog(
    private val listener: GotoKittyListener
) : DialogFragment() {

    companion object {
        const val TAG = "GotoKittyDialog"
    }

    interface GotoKittyListener {
        fun onGotoKitty(position: Int)
    }

    private lateinit var binding: DialogGotoKittyBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogGotoKittyBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Kitty to Collection")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                listener.onGotoKitty(Integer.parseInt(binding.positionText.text.toString()))
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
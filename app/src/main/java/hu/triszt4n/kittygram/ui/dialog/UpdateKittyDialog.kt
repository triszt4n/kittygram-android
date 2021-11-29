package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.View

import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.R
import hu.triszt4n.kittygram.data.entity.Kitty
import hu.triszt4n.kittygram.databinding.DialogAddOrUpdateKittyBinding

class UpdateKittyDialog(
    private val listener: UpdateKittyListener
) : DialogFragment() {

    companion object {
        const val TAG = "UpdateKittyDialog"
    }

    interface UpdateKittyListener {
        fun onSaveKitty(kitty: Kitty)
    }

    private lateinit var binding: DialogAddOrUpdateKittyBinding
    private lateinit var kitty: Kitty

    fun addKitty(kitty: Kitty): UpdateKittyDialog {
        this.kitty = kitty
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddOrUpdateKittyBinding.inflate(layoutInflater)

        binding.kittyName.apply {
            setText(kitty.name)
            addTextChangedListener { text: Editable? ->
                if (text.toString().length < 4) {
                    binding.kittyName.error = getString(R.string.warning_name_too_short)
                }
            }
        }

        binding.kittyRating.rating = kitty.rating?.toFloat() ?: 0.0f

        binding.collectionsSpinnerLabel.visibility = View.GONE
        binding.collectionsSpinner.visibility = View.GONE

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.prompt_update_kitty))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                kitty.name = binding.kittyName.text.toString()
                kitty.rating = binding.kittyRating.rating.toInt()
                listener.onSaveKitty(kitty)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
}
package hu.triszt4n.kittygram.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.triszt4n.kittygram.R
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
            .setTitle(getString(R.string.prompt_goto_kitty))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                listener.onGotoKitty(Integer.parseInt(binding.positionText.text.toString()))
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
}
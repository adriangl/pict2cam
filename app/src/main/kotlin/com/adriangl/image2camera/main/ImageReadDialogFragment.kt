package com.adriangl.image2camera.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.adriangl.image2camera.R

class ImageReadDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): ImageReadDialogFragment {
            return ImageReadDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.image_read_dialog_fragment, null)
        val builder = AlertDialog.Builder(activity)
                .setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

}

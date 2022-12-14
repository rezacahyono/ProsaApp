package com.rchyn.prosa.components

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rchyn.prosa.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) {

    private var _binding: DialogLoadingBinding? = null
    private val binding get() = _binding as DialogLoadingBinding

    private val dialog: AlertDialog

    init {
        _binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        val builder = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(false)
        dialog = builder.create()
    }

    fun startLoading() {
        dialog.show()
    }


    fun dismissLoading() {
        dialog.cancel()
        _binding = null
    }
}
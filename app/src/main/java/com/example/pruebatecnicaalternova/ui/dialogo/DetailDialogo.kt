package com.example.pruebatecnicaalternova.ui.dialogo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.pruebatecnicaalternova.R
import com.example.pruebatecnicaalternova.data.local.AdminSQLiteStore
import com.example.pruebatecnicaalternova.databinding.DialogoDetailBinding
import com.example.pruebatecnicaalternova.ui.Router
import com.example.pruebatecnicaalternova.ui.screenChange

class DetailDialogo(contexto: Context) : DialogFragment() {

    // region inicializar variables

    private var contexDialogo :Context = contexto
    private var binding: DialogoDetailBinding? = null
    private lateinit var bdStore: AdminSQLiteStore

    // endregion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogoDetailBinding.inflate(inflater, container, false)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.borders_card_store)

        bdStore = AdminSQLiteStore(contexDialogo)
        initEvent()

        return binding?.root
    }

    // region funcion para evento de clic

    private fun initEvent() {
        binding?.btnCancelBuy?.setOnClickListener {
            dismiss()
        }
        binding?.btnDeleteBuy?.setOnClickListener {
            bdStore.deleteBuys()
            Router().goToScreens(screenChange.HOME, contexDialogo as Activity, false)
            dismiss()
        }
    }
    // endregion

}
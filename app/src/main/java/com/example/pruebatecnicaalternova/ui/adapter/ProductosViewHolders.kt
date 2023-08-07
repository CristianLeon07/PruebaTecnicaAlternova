package com.example.pruebatecnicaalternova.ui.adapter

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pruebatecnicaalternova.R
import com.example.pruebatecnicaalternova.Utility
import com.example.pruebatecnicaalternova.Utility.Companion.productSelectGlobal
import com.example.pruebatecnicaalternova.data.external.ProductsAll
import com.example.pruebatecnicaalternova.databinding.ItemProductsBinding
import com.example.pruebatecnicaalternova.ui.Router
import com.example.pruebatecnicaalternova.ui.detail.DetailActivity
import com.example.pruebatecnicaalternova.ui.home.HomeActivity
import com.example.pruebatecnicaalternova.ui.screenChange

class ProductosViewHolders(view: View) : RecyclerView.ViewHolder(view) {
    // region inicializar la vista del adaptador de prodcutos
    private val binding = ItemProductsBinding.bind(view)

    // endregion

    // region funcion que pinta datos de la lista  y tiene enventod para los productos que se seleccionen

    fun render(products: ProductsAll, activity: Activity) {
        binding.tvName.text = products.name
        binding.tvId.text = products.id.toString()
        binding.tvPrice.text = String.format("$ %s", products.unit_price.toString())
        Glide.with(itemView.context)
            .load(products.image)
            .error(R.mipmap.domi)
            .into(binding.imgImage)

        binding.btnRemoveProduct.setOnClickListener {
            Utility().countProduct(2, binding.tvCountProduct)
        }

        binding.btnAddProduct.setOnClickListener {
            Utility().countProduct(1, binding.tvCountProduct)

        }

        binding.tvBuy.setOnClickListener {
            Utility().insertDataBD(products, activity)
            HomeActivity().updateAllProducts()
            Router().goToScreens(screenChange.HOME, activity, false)

        }

        binding.cv1.setOnClickListener {
            productSelectGlobal = products
            try {
                var ok = activity as DetailActivity
                Router().goToScreens(screenChange.DETAIL, ok, false)
            } catch (e: Exception) {
                Router().goToScreens(screenChange.DETAIL, activity, false)
                Log.i("entro aca", e.message.toString())
            }
        }
    }
    // endregion
}
package com.example.pruebatecnicaalternova.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebatecnicaalternova.R
import com.example.pruebatecnicaalternova.data.external.ProductsAll

class ProductosAdapter(var productList: List<ProductsAll>, var activity: Activity): RecyclerView.Adapter<ProductosViewHolders>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolders {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductosViewHolders(layoutInflater.inflate(R.layout.item_products, parent, false))
    }

    override fun onBindViewHolder(holder: ProductosViewHolders, position: Int) {

        val item = productList[position]
        holder.render(item,activity)
    }

    override fun getItemCount(): Int = productList.size
}
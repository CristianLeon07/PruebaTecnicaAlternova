package com.example.pruebatecnicaalternova

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.pruebatecnicaalternova.data.external.ProductsAll
import com.example.pruebatecnicaalternova.data.local.AdminSQLiteStore
import java.text.NumberFormat
import java.util.*

class Utility {
    // region  las funciones y variables creadas en esta clase, se pueden reutilizar en diferentes vistas

    // region declaracion de variables staticas que se utiliaran en multiples activitys
    companion object {
        var productSelectGlobal: ProductsAll? = null
        var amountProduct = 1
        private lateinit var bdStore: AdminSQLiteStore

    }
    // endregion

    // region funcion para insertar datos en la base de datos local
    fun insertDataBD(products: ProductsAll,activity: Activity){

        bdStore = AdminSQLiteStore(activity)
        var subtotal: Int = amountProduct * products.unit_price!!
        bdStore.insertData(
            products.id,
            amountProduct,
            products.name,
            products.unit_price,
            products.image,
            products.description,
            subtotal)
    }
    // endregion

    // region funcion para actualizar la cantidad de producto que se seleccione

    fun countProduct(countProducts: Int, textView: TextView) {
        amountProduct = textView.text.toString().toInt()

        if (countProducts == 1) {
            amountProduct++
        } else if (countProducts == 2 && amountProduct > 1) {
            amountProduct--
        }

        textView.text = amountProduct.toString()
    }
    // endregion

    // region funcion que convierte el valor de price en formato moneda

    fun converterPrice(price: Int?): String {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(price).replace(",", ".")
    }

    // endregion

    // region funcion que levanta el dialogo cuando se realiza una consulta al servidor

    fun ShowLoadingDialog(context: Context?): AlertDialog? {

        val alertDialog: AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.carga_blanca)
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.loading_data, null)
        builder.setView(customLayout)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
        return alertDialog
    }

    // endregion

    // region funcion que cierra el dialogo despues de retorna  una respuesta a una consulta echa al servidor

    fun CloseLoadingDialog(dialog: AlertDialog?) {
        var dialog = dialog
        if (dialog != null) {
            if (dialog.isShowing) {

                val context = (dialog.context as ContextWrapper).baseContext

                if (context is Activity) {

                    // Api >=17
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!context.isFinishing && !context.isDestroyed) {
                           dialog.dismiss()
                        }
                    } else {

                        // Api < 17. Unfortunately cannot check for isDestroyed()
                        if (!context.isFinishing) {
                            dialog.dismiss()
                        }
                    }
                } else  // if the Context used wasn't an Activity, then dismiss it too
                    dialog.dismiss()
            }
        }
    }
    // endregion

    // endregion
}
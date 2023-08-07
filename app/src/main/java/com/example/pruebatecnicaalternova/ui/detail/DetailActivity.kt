package com.example.pruebatecnicaalternova.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pruebatecnicaalternova.R
import com.example.pruebatecnicaalternova.Utility
import com.example.pruebatecnicaalternova.Utility.Companion.productSelectGlobal
import com.example.pruebatecnicaalternova.data.external.APIConecction
import com.example.pruebatecnicaalternova.data.external.APIService
import com.example.pruebatecnicaalternova.data.external.ProductsAll
import com.example.pruebatecnicaalternova.data.external.endpoint
import com.example.pruebatecnicaalternova.databinding.ActivityDetailBinding
import com.example.pruebatecnicaalternova.ui.Router
import com.example.pruebatecnicaalternova.ui.home.HomeActivity
import com.example.pruebatecnicaalternova.ui.screenChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    // region incializar variables

    private lateinit var binding: ActivityDetailBinding
    private lateinit var dialogo: AlertDialog

    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callDetailProduct()
        initEvent()
    }

    // region funcion para  consultar a la APIque retornara el detalle de procuto seleccionado
    private fun callDetailProduct() {
        dialogo = Utility().ShowLoadingDialog(this) as AlertDialog
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = APIConecction().getData().create(APIService::class.java)
                    .getApi(endpoint().ENDPOINT_DETAIL, productSelectGlobal?.id!!)
                Log.i("error", call.toString())
                runOnUiThread {
                    Utility().CloseLoadingDialog(dialogo)
                    productSelectGlobal = call?.body()
                    initUI(productSelectGlobal)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.i("ErrorConsultaApiAllProducts", e.message.toString())
                }
            }
        }
    }
    // endregion

    // region funcion para mostrar los datos de la consulta en la pantalla

    private fun initUI(productSelectGlobal: ProductsAll?) {
        binding.btnAtras.text = String.format("%s", "<Regresar")
        binding.tvName.text = productSelectGlobal?.name
        binding.tvDescription.text = String.format("Descripción : %s",productSelectGlobal?.description)
        binding.tvId.text = String.format("%s", productSelectGlobal?.id)
        binding.tvPrice.text =
            String.format("$ %s", Utility().converterPrice(productSelectGlobal?.unit_price))
        binding.tvStock.text = String.format("%s", productSelectGlobal?.stock)

        Glide.with(this)
            .load(productSelectGlobal?.image)
            .error(R.mipmap.domi)
            .into(binding.fotoDetalle)
    }
    // endregion

    // region funcion de eventos de click

    private fun initEvent() {
        binding.btnAtras.setOnClickListener {
            onBackPressed()
        }
        binding.btnAddProduct.setOnClickListener {
            Utility().countProduct(1, binding.tvCountProduct)
        }

        binding.btnRemoveProduct.setOnClickListener {
            Utility().countProduct(2, binding.tvCountProduct)
        }

        binding.btnBuy.setOnClickListener {
            Utility().insertDataBD(productSelectGlobal!!, this)
            HomeActivity().updateAllProducts()
            Router().goToScreens(screenChange.HOME, this, false)
        }
    }
    // endregion
}
package com.example.pruebatecnicaalternova.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebatecnicaalternova.Utility
import com.example.pruebatecnicaalternova.data.external.APIConecction
import com.example.pruebatecnicaalternova.data.external.APIService
import com.example.pruebatecnicaalternova.data.external.ProductsAll
import com.example.pruebatecnicaalternova.data.external.endpoint
import com.example.pruebatecnicaalternova.data.local.AdminSQLiteStore
import com.example.pruebatecnicaalternova.databinding.ActivityHomeBinding
import com.example.pruebatecnicaalternova.ui.Router
import com.example.pruebatecnicaalternova.ui.adapter.ProductosAdapter
import com.example.pruebatecnicaalternova.ui.dialogo.DetailDialogo
import com.example.pruebatecnicaalternova.ui.screenChange
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    // region declaracion de variables

    /**
     * Variable para instanciar la View usando bindig
     */
    private lateinit var binding: ActivityHomeBinding

    /**
     * Variable para instanciar el servicio de autenticacion de firebase
     */
    private lateinit var auth: FirebaseAuth

    /**
     * Variable para instanciar el dialogo que se utiliza en las consultas
     */
    private lateinit var dialogo: AlertDialog

    /**
     * Variable para instanciar la base de datos local
     */
    private lateinit var bdStore: AdminSQLiteStore

    /**
     * Variable para instanciar el adaptador de productos
     */
    lateinit var adapterProductsAll: ProductosAdapter

    /**
     * Variable para instanciar la lista que se llenara con lo que retorne la consulta
     */
    private var products: List<ProductsAll>? = emptyList()
    // endregion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Se inicializa la variable auth y la base de datos local
         */
        auth = FirebaseAuth.getInstance()
        bdStore = AdminSQLiteStore(this)

        init()
        initEvent()
    }


    // region función  para inicializar variables, bindear datos y mostrarlos en pantalla, lanzar metoddos.
    private fun init() {
        val name = auth.currentUser?.displayName
        if (name?.isNotEmpty() == true) {
            val string: List<String> = name.split(" ")
            val resultName = string[0] + " " + string[1]
            binding.tvInfoUser.text = String.format("Bienvenido:  %s ", resultName)
        }
        binding.rcwProducts.setHasFixedSize(true)
        binding.rcwProducts.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterProductsAll = ProductosAdapter(products ?: emptyList(), this)
        binding.rcwProducts.adapter = adapterProductsAll
        queryAllProducts()
        updateResumenHome()
    }
    // endregion

    // region funcion para lanzar eventos de click

    private fun initEvent() {
        binding.btnSignOff.setOnClickListener {

            try {
                FirebaseAuth.getInstance().signOut()
                Router().goToScreens(screenChange.SPLAHS, this, false)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                finish()
            } catch (e: Exception) {
                Router().goToScreens(screenChange.SPLAHS, this, false)
            }
        }
        binding.rlContainerPriceBuy.setOnClickListener {
            DetailDialogo(this).show(supportFragmentManager, "custom_dialog")
        }
    }


    // endregion

    // region funcion para consultar los datos de la API que se mostraran en la pantalla

    private fun queryAllProducts() {
        dialogo = Utility().ShowLoadingDialog(this) as AlertDialog
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = APIConecction().getData().create(APIService::class.java)
                    .getApiArray(endpoint().ENDPOINT_ALLPRODUCTS)
                Log.i("error", call.toString())
                runOnUiThread {
                    Utility().CloseLoadingDialog(dialogo)
                    productsChange(call?.body()?.data)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Utility().CloseLoadingDialog(dialogo)
                    Log.i("ErrorConsultaApiAllProducts", e.message.toString())
                }
            }
        }
    }

    // endregion

    // region funcion que se llama dentro de la consulta para converir los datos del objeto en una lista del mismo

    private fun productsChange(data: JsonArray?) {
        if (data != null) {
            products = Gson().fromJson(data, Array<ProductsAll>::class.java).toList()
            adapterProductsAll.productList = products ?: emptyList()
            adapterProductsAll.notifyDataSetChanged()
        }
    }
    // endregion

    // region funcion extra, se consulta a la base de datos si productos agregados y  actualiza la vista con dicha compra realizada

    private fun updateResumenHome() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = bdStore.consultQuantityProduct()
                runOnUiThread {

                    if (call > 0) {
                        binding.tvContadorTotalHome.text = String.format("%s", call)
                        binding.rlContainerPriceBuy.visibility = View.VISIBLE
                        binding.tvTotalCompraHome.text = String.format(
                            "$ %s",
                            Utility().converterPrice(
                                bdStore.consultTotalPriceProduct()
                            )
                        )
                    } else {
                        binding.rlContainerPriceBuy.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.i("ErrorConsultaBD", e.message.toString())
                }
            }
        }
    }
    // endregion

    // region funcion consulta a la API para actualizar la vista con los mismos prodcutos

    fun updateAllProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = APIConecction().getData().create(APIService::class.java)
                    .postApiArray(endpoint().ENDPOINT_BUY)
                Log.i("error", call.toString())
                runOnUiThread {
                    updateProductsChange(call?.body()?.data)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Utility().CloseLoadingDialog(dialogo)
                    Log.i("ErrorConsultaApiAllProducts", e.message.toString())
                }
            }
        }
    }

    // endregion

    // region funcion que se llama en la segunda consulta a la API y sirve para convertir la respuesta en una lista y notificar el cambio

    private fun updateProductsChange(data: JsonArray?) {
        products = emptyList()
        adapterProductsAll = ProductosAdapter(products ?: emptyList(), this)
        if (data != null) {
            products = Gson().fromJson(data, Array<ProductsAll>::class.java).toList()
            adapterProductsAll.productList = products ?: emptyList()
            adapterProductsAll.notifyDataSetChanged()
        }

    }
    // endregion
}
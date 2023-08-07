package com.example.pruebatecnicaalternova.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteStore(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "store.db"
        private const val DATABASE_VERSION = 1
    }

    object MyTableContract {
        const val TABLE_BUY_PRODUCT = "buyProduct"
        const val COLUMN_ID = "idProduct"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_NAME_PRODUCT = "nameProduct"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_SUBTOTAL = "subTotal"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Define la estructura de la tabla y otros objetos necesarios
        db?.execSQL("CREATE TABLE IF NOT EXISTS buyProduct ( idProduct INTEGER, amount INTEGER, nameProduct TEXT, price INTEGER, image TEXT,description TEXT, subTotal INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2 && newVersion >= 2) {
        }
    }

    fun insertData(
        idProduct: Int?,
        amount: Int?,
        nameProduct: String?,
        price: Int?,
        image: String?,
        description: String,
        subTotal: Int?
    ) {
        val contentValues = ContentValues()
        contentValues.put(MyTableContract.COLUMN_ID, idProduct)
        contentValues.put(MyTableContract.COLUMN_AMOUNT, amount)
        contentValues.put(MyTableContract.COLUMN_NAME_PRODUCT, nameProduct)
        contentValues.put(MyTableContract.COLUMN_PRICE, price)
        contentValues.put(MyTableContract.COLUMN_IMAGE, image)
        contentValues.put(MyTableContract.COLUMN_DESCRIPTION, description)
        contentValues.put(MyTableContract.COLUMN_SUBTOTAL, subTotal)

        // Obtén una instancia de la base de datos en modo escritura
        val db = this.writableDatabase

        // Inserta los datos en la tabla
        db.insert(MyTableContract.TABLE_BUY_PRODUCT, null, contentValues)

        // Cierra la base de datos
        //db.close()
    }

    fun consultQuantityProduct(): Int {
        var retorno = 0
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(${MyTableContract.COLUMN_AMOUNT})FROM ${MyTableContract.TABLE_BUY_PRODUCT} ",
            null
        )
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) == null) return 0
                retorno += cursor.getString(0).toInt()
            } while (cursor.moveToNext())
        }
        return retorno
    }

    fun consultTotalPriceProduct(): Int {
        var retorno = 0
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(subTotal) FROM buyProduct ", null)
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) == null) return 0
                retorno += cursor.getString(0).toInt()
            } while (cursor.moveToNext())
        }
        return retorno
    }

    fun deleteBuys(): String? {
        val db: SQLiteDatabase = this.readableDatabase
        var mensaje = ""
        val cantidad = db.delete(MyTableContract.TABLE_BUY_PRODUCT, null, null)
        mensaje = if (cantidad != 0) {
            "Eliminado correctamente"
        } else {
            "No hay registros que eliminar"
        }
        return mensaje
    }
}

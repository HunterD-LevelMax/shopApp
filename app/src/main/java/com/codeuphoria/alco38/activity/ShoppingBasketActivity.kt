package com.codeuphoria.alco38.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.codeuphoria.alco38.*
import com.codeuphoria.alco38.adapter.ProductAdapterShop
import com.codeuphoria.alco38.databinding.ActivityShoppingBasketBinding
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.thread

class ShoppingBasketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingBasketBinding

    private val adapter = ProductAdapterShop(productListInShop)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jsonString = createJsonString()

        binding.apply {

            costTextView.text = "Итого: $costShop рублей (с учетом доставки)"


           buttonAcceptOrder.setOnClickListener {
               checkMinShop()
           }
       }
        init()
    }

    private fun checkMinShop(){
        if (costShop<=990){
            showToast("Выберите продуктов на сумму от 690 рублей!")
        }else{
            sendOrder()
            showToast("Заказ оформлен. С вами свяжется наш оператор")
            replaceActivity(TestJsonActivity())
        }
    }


    private fun getNumber() {

    }

    private fun createJsonString(): String {
        val jsonRes = JSONObject()
        val jArray = JSONArray()
        for ( i in 0 until productListInShop.size) {
            val jGroup = JSONObject()
            jGroup.put("title", productListInShop[i].title)
            jGroup.put("price", productListInShop[i].price)
            jGroup.put("amount",productListInShop[i].amount)

            jArray.put(jGroup)
        }
        val order = JSONObject()
        order.put("number", "89999999999")
        order.put("date", Calendar.getInstance().time)
        jArray.put(order)

        jsonRes.put("order", jArray)
        Log.d("Server", jsonRes.toString())

        return jsonRes.toString()
    }


    private fun sendOrder(){
        thread {
            val okHttpClient = OkHttpClient()

            val requestBody = FormBody.Builder()
                .add("order", jsonString)
                .build()

            val request = Request.Builder()
                .url(URL_POST)
                .method("POST", requestBody)
                .build()

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread{
                        showToast("Error")
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread{
                        showToast("Ok")
                    }

                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        binding.apply {
            recyclerOrder.layoutManager = GridLayoutManager(this@ShoppingBasketActivity, 2)
            recyclerOrder.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                replaceActivity(SettingsActivity())
                showToast("Настройки")
            }
            R.id.terms_of_use -> {
                replaceActivity(TermsOfUseActivity())
            }
            R.id.about -> {
                val versionApp = resources.getString(R.string.versionApp)
                showToast("Версия $versionApp")
            }
        }
        return true
    }
}



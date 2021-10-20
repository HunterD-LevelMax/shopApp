package com.codeuphoria.alco38.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.codeuphoria.alco38.*
import com.codeuphoria.alco38.adapter.ProductAdapter
import com.codeuphoria.alco38.data.Product
import com.codeuphoria.alco38.databinding.ActivityMainBinding
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = ProductAdapter(productList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun checkShopList(): Boolean {
        return true
    }

    private fun init() {

        binding.apply {
            buttonShopBasket.setOnClickListener {
                if (checkShopList()) {
                    replaceActivity(ShoppingBasketActivity())
                } else {
                    showToast(getString(R.string.choose_product))
                }
            }
            buttonRefresh.setOnClickListener {
                requestServer()
                buttonRefresh.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }

            recyclerProducts.layoutManager = GridLayoutManager(this@MainActivity, 1)
            recyclerProducts.adapter = adapter
        }

        createUUID()
        requestServer()
    }

    private fun requestServer() {
        thread {
            val request = Request.Builder()
                .url(URL)
                .addHeader("User-Agent", userAgentAndroid!!)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread() {
                        setVisibleView(false)
                        showToast("Сервер недоступен. Повторите позже")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonString = response.body!!.string()
                    val jsonObject = JSONObject(jsonString)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("products")

                    runOnUiThread {
                        setVisibleView(true)
                        pushData(jsonArray)
                    }
                }
            })
        }
    }


    private fun setVisibleView(flag: Boolean) {
        when (flag) {
            false -> binding.apply {
                progressBar.visibility = View.GONE
                buttonRefresh.visibility = View.VISIBLE
            }
            true -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun pushData(jsonArray: JSONArray) {
        for (i in 0 until jsonArray.length()) {
            val product = Product(
                imageIdList[i],
                jsonArray.getJSONObject(i).getString("title"),
                jsonArray.getJSONObject(i).getString("price"),
                0
            )
            adapter.addProduct(product)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun animationShopCount() {
        binding.apply {
            buttonShopBasket.startAnimation(
                AnimationUtils.loadAnimation(
                    this@MainActivity,
                    R.anim.pulse
                )
            )
            countShopTextView.startAnimation(
                AnimationUtils.loadAnimation(
                    this@MainActivity,
                    R.anim.pulse
                )
            )
        }
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
            R.id.clean_shop -> {
                productListInShop.clear()
                costShop = 0
                showToast("Корзина очищена")
            }
        }
        return true
    }

    private fun createUUID() {
        uuid = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            .getString("UUID", "empty").toString()

        if (uuid == "empty") {
            uuid = UUID.randomUUID().toString()
            saveUUID(uuid)
        }
    }

    private fun saveUUID(uuid: String) {
        val sharedPreferences: SharedPreferences? =
            this.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.apply { putString("UUID", uuid) }?.apply()
    }

    override fun onResume() {
        super.onResume()
        adapter.updateList()
    }
}

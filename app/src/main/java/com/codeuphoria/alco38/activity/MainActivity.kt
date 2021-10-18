package com.codeuphoria.alco38.activity

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
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter = ProductAdapter(productList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonShopBasket.setOnClickListener {

                if (checkShopList()) {
                    replaceActivity(ShoppingBasketActivity())
                } else {
                    showToast(getString(R.string.choose_product))
                }
            }
        }

        init()
        requestServer()
    }

    fun checkShopList(): Boolean {
        return productListInShop.size != 0
    }

    private fun init() {
        binding.apply {
            recyclerProducts.layoutManager = GridLayoutManager(this@MainActivity, 1)
            recyclerProducts.adapter = adapter
        }
    }

    fun requestServer() {
        thread {
            val request = Request.Builder()
                .url(URL)
                .addHeader("User-Agent", userAgentAndroid!!)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread() {
                        showToast("Сервер недоступен. Повторите позже")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    var jsonString = response.body!!.string()
                    var jsonObject = JSONObject(jsonString)
                    var jsonArray: JSONArray = jsonObject.getJSONArray("products")

                    runOnUiThread {
                        pushData(jsonArray)

                        binding.apply {
                            progressBar.visibility = View.GONE
                        }

                    }
                }
            })
        }
    }

    fun pushData(jsonArray: JSONArray) {
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

    fun animationShopCount() {
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
            R.id.clean_shop ->{
                showToast("Корзина очищена")
                productListInShop.clear()
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        adapter.updateList()
    }
}

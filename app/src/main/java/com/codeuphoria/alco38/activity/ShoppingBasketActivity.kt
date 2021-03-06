package com.codeuphoria.alco38.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.codeuphoria.alco38.*
import com.codeuphoria.alco38.adapter.ProductAdapterShop
import com.codeuphoria.alco38.databinding.ActivityShoppingBasketBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.thread


class ShoppingBasketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingBasketBinding
    private lateinit var number: String
    private val adapter = ProductAdapterShop(productListInShop)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            costTextView.text = "Итого: $costShop рублей (без учета доставки)"

            buttonAcceptOrder.setOnClickListener {
                //checkMinShop()
                if (productListInShop.size != 0) {
                    showDialogNumber()
                    costShop + 300
                }else{
                    showToast("Добавь понравившийся товар")
                }
            }
        }
        init()
    }


    private fun checkMinShop() {
        if (costShop <= 990) {
            showToast("Выберите продуктов на сумму от 690 рублей!")
        } else {
            //showDialog()
        }
    }


    private fun showDialogNumber() {
        val mBottomSheetDialog = BottomSheetDialog(this)
        val sheetView: View =
            this.layoutInflater.inflate(R.layout.number_dialog, null)
        mBottomSheetDialog.setContentView(sheetView)

        val editText: EditText = sheetView.findViewById(R.id.input_number)
        val buttonSend: Button = sheetView.findViewById(R.id.button_send)

        mBottomSheetDialog.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        mBottomSheetDialog.show()

        number = loadPhoneNumber()

        if (number != "empty") {
            editText.setText(number)
        }

        buttonSend.setOnClickListener {
            number = editText.text.toString()
            savePhoneNumber(number)
            jsonString = createJsonString(number)
            sendOrder()
            mBottomSheetDialog.dismiss()
        }
    }

    private fun loadPhoneNumber(): String {
        return getSharedPreferences(PREFERENCE, MODE_PRIVATE)
            .getString("PHONE_NUMBER", "empty").toString()
    }

    private fun savePhoneNumber(number: String) {
        val sharedPreferences: SharedPreferences? =
            this.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.apply { putString("PHONE_NUMBER", number) }?.apply()
    }

    private fun getCurrentTimestamp(): String {
        return System.currentTimeMillis().toString()
    }

    private fun createJsonString(number: String): String {
        val jArray = JSONArray()
        val orderDate = JSONObject()
        val jsonRes = JSONObject()

        for (i in 0 until productListInShop.size) {
            val jGroup = JSONObject()
            jGroup.put("title", productListInShop[i].title)
            jGroup.put("price", productListInShop[i].price)
            jGroup.put("amount", productListInShop[i].amount)
            jArray.put(jGroup)
        }
        orderDate.put("number", number)
        orderDate.put("date", getCurrentTimestamp())
        jArray.put(orderDate)

        jsonRes.put("order", jArray)
        Log.d("Server", jsonRes.toString())
        return jsonRes.toString()
    }

    private fun sendOrder() {
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
                    runOnUiThread {
                        showToast("К сожалению сервер временно недоступен")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        showToast("Заказ оформлен. С вами свяжется наш оператор")
                    }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        binding.apply {
            if (productListInShop.size == 0) {
                titleEmptyShop.visibility = View.VISIBLE
            } else {
                titleEmptyShop.visibility = View.GONE
            }
            recyclerOrder.layoutManager = GridLayoutManager(this@ShoppingBasketActivity, 1)
            recyclerOrder.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
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
                adapter.notifyDataSetChanged()
                finish()
            }
        }
        return true
    }
}



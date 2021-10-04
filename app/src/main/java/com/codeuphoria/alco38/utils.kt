package com.codeuphoria.alco38

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codeuphoria.alco38.data.Product
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

const val PREFERENCE = "PREFERENCE"

val versionCode = System.getProperty("http.agent").toString() + "\n" + "(с) Все права защищены"

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    //this.finish()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

var costShop: Int = 300 // с учетом доставки

var productList = ArrayList<Product>()

var productListInShop = ArrayList<Product>()

var jsonString: String = ""

var imageIdList = listOf(
    R.drawable.cola,
    R.drawable.potato_chips,
    R.drawable.snack,
    R.drawable.beer,
    R.drawable.wine_icon
)

var userAgentAndroid: String? = System.getProperty("http.agent")

const val URL = "https://alco38.ru/example.php"
const val URL_POST = "https://alco38.ru/new_order.php"




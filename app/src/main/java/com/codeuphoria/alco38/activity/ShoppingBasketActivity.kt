package com.codeuphoria.alco38.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.codeuphoria.alco38.R
import com.codeuphoria.alco38.adapter.ProductAdapter
import com.codeuphoria.alco38.data.Product
import com.codeuphoria.alco38.databinding.ActivityShoppingBasketBinding

class ShoppingBasketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingBasketBinding

    private val adapter = ProductAdapter()

    private val imageIdList = listOf(
        R.drawable.cola,
        R.drawable.potato_chips,
        R.drawable.snack,
        R.drawable.beer,
        R.drawable.wine_icon
    )

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init() {
        binding.apply {
            recyclerOrder.layoutManager = GridLayoutManager(this@ShoppingBasketActivity, 3)
            recyclerOrder.adapter = adapter

            for(i in 1..10){
                if (index > imageIdList.size - 1) index = 0
                val product = Product(imageIdList[index], "Product $index", "100 рублей")
                adapter.addProduct(product)
                index++
            }

        }
    }
}



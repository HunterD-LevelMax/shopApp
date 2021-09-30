package com.codeuphoria.alco38.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.codeuphoria.alco38.R
import com.codeuphoria.alco38.adapter.ProductAdapter
import com.codeuphoria.alco38.data.Product
import com.codeuphoria.alco38.databinding.ActivityMainBinding
import com.codeuphoria.alco38.replaceActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonShopBasket = binding.buttonShopBasket
        val countShop = binding.countShop
        val recyclerProducts = binding.recyclerProducts

        buttonShopBasket.setOnClickListener {
            replaceActivity(ShoppingBasketActivity())
        }

        init()
    }

    private fun init() {
        binding.apply {
            recyclerProducts.layoutManager = GridLayoutManager(this@MainActivity, 3)
            recyclerProducts.adapter = adapter

            for(i in 1..10){
                if (index > imageIdList.size - 1) index = 0
                val product = Product(imageIdList[index], "Product $index", "100 рублей")
                adapter.addProduct(product)
                index++
            }

            buttonAdd.setOnClickListener {
                    if (index > imageIdList.size - 1) index = 0
                    val product = Product(imageIdList[index], "Product $index", "100 рублей")
                    adapter.addProduct(product)
                    index++
                animationShopCount()
            }
        }
    }


    private fun animationShopCount() {
        binding.countShop.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse))
    }


}

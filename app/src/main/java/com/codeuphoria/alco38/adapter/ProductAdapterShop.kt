package com.codeuphoria.alco38.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeuphoria.alco38.R
import com.codeuphoria.alco38.data.Product
import com.codeuphoria.alco38.databinding.ProductItemShopBinding

class ProductAdapterShop(private val arrayProduct: ArrayList<Product>): RecyclerView.Adapter<ProductAdapterShop.ProductHolderShop>() {

    class ProductHolderShop(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ProductItemShopBinding.bind(item)

        fun bind(product: Product) = with(binding) {
            itemImageShop.setImageResource(product.imageId)
            itemTitleShop.text = product.title
            itemPriceShop.text = product.price
            amountProductShop.text = product.amount.toString()
        }
    }


    fun addProduct(product: Product) {
        arrayProduct.add(product)
        updateList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolderShop {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_shop, parent, false)
        return ProductHolderShop(view)
    }

    override fun onBindViewHolder(holder: ProductHolderShop, position: Int) {
        holder.bind(arrayProduct[position])
    }

    override fun getItemCount(): Int {
        return arrayProduct.size
    }


}
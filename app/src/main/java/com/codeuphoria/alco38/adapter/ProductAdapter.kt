package com.codeuphoria.alco38.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeuphoria.alco38.R
import com.codeuphoria.alco38.data.Product
import com.codeuphoria.alco38.databinding.ProductItemBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    val productList = ArrayList<Product>()


    class ProductHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ProductItemBinding.bind(item)
        fun bind(product: Product) = with(binding) {
            itemImage.setImageResource(product.imageId)
            itemTitle.text = product.title
            itemPrice.text = product.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun addProduct(product: Product) {
        productList.add(product)
        notifyDataSetChanged()
    }
}
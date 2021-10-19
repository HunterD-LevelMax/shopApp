package com.codeuphoria.alco38.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeuphoria.alco38.R
import com.codeuphoria.alco38.costShop
import com.codeuphoria.alco38.data.Product
import com.codeuphoria.alco38.databinding.ProductItemBinding
import com.codeuphoria.alco38.productListInShop




class ProductAdapter(private val arrayProduct: ArrayList<Product>): RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    class ProductHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ProductItemBinding.bind(item)
        fun bind(product: Product) = with(binding) {
            itemImage.setImageResource(product.imageId)
            itemTitle.text = product.title
            itemPrice.text = product.price


            buttonAddProduct.setOnClickListener {
                amountProduct.text = product.amount.toString()
                productListInShop.
                add(Product(
                    product.imageId,
                    product.title,
                    product.price,
                    1))

                costShop += product.price.toInt()
            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(arrayProduct[position])
    }

    override fun getItemCount(): Int {
        return arrayProduct.size
    }


}
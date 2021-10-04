import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.codeuphoria.alco38.R
import com.codeuphoria.alco38.activity.MainActivity
import com.codeuphoria.alco38.costShop
import com.codeuphoria.alco38.data.Product
import com.codeuphoria.alco38.databinding.ProductItemBinding
import com.codeuphoria.alco38.productListInShop

class ProductAdapter(private val arrayProduct: ArrayList<Product>): RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    class ProductHolder(item: View, val arrayProduct: java.util.ArrayList<Product>) : RecyclerView.ViewHolder(item) {
        val binding = ProductItemBinding.bind(item)
        fun bind(product: Product) = with(binding) {
            itemImage.setImageResource(product.imageId)
            itemTitle.text = product.title
            itemPrice.text = product.price


            buttonAddProduct.setOnClickListener {
                amountProduct.text = product.amount.toString()
                //ProductAdapter().notifyItemChanged(adapterPosition)
                // Log.d("Update", adapterPosition.toString())

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

    @SuppressLint("NotifyDataSetChanged")
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
        return ProductHolder(view,arrayProduct)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(arrayProduct[position])
    }

    override fun getItemCount(): Int {
        return arrayProduct.size
    }


}
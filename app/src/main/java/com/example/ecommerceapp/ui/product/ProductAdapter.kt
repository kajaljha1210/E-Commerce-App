package com.example.ecommerceapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.remote.Product


class ProductAdapter(
    private var list: List<Product>,
    private val onAddToCart: (Product) -> Unit,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProduct)
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val btnAdd: Button = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = list[position]

        val context = holder.itemView.context
        val resId = context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)

        if (resId != 0) {
            holder.img.setImageResource(resId) // ✅ drawable mil gaya
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher_foreground) // fallback image
        }

        holder.name.text = product.name
        holder.price.text = "₹${product.price}"
        holder.description.text = "₹${product.description}"

        holder.itemView.setOnClickListener { onItemClick(product) }
        holder.btnAdd.setOnClickListener { onAddToCart(product) }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Product>) {
        list = newList
        notifyDataSetChanged()
    }
}
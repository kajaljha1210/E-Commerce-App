package com.example.ecommerceapp.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.remote.CartItem

class CartAdapter(
    private var list: List<CartItem>,
    private val onQtyChange: (CartItem, Int) -> Unit,
    private val onRemove: (CartItem) -> Unit,
    private val onItemClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val etQty: EditText = view.findViewById(R.id.etQty)
        val btnRemove: Button = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = list[position]

        holder.tvName.text = item.name
        holder.tvPrice.text = "$${item.price * item.quantity}"
        holder.etQty.setText(item.quantity.toString())

        holder.etQty.addTextChangedListener {
            val newQty = it.toString().toIntOrNull() ?: 1
            if (newQty != item.quantity) onQtyChange(item, newQty)
        }

        holder.btnRemove.setOnClickListener { onRemove(item) }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<CartItem>) {
        list = newList
        notifyDataSetChanged()
    }
}

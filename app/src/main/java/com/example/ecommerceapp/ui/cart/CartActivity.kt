package com.example.ecommerceapp.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.remote.CartItem
import com.example.ecommerceapp.ui.PreviewActivity
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartAdapter

    private lateinit var rvCart: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTax: TextView
    private lateinit var tvGrandTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var tvEmptyCart: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initViews()
        setupToolbar()
        setupRecyclerView()
        observeCartItems()
        setupCheckout()
    }

    private fun initViews() {
        rvCart = findViewById(R.id.rvCart)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvTax = findViewById(R.id.tvTax)
        tvGrandTotal = findViewById(R.id.tvGrandTotal)
        btnCheckout = findViewById(R.id.btnCheckout)
        tvEmptyCart = findViewById(R.id.tvEmptyCart)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Your Cart"
            setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(
            emptyList(),
            onQtyChange = { item, qty -> viewModel.updateQuantity(item, qty) },
            onRemove = { item -> viewModel.removeItem(item) },
            onItemClick = { item -> openPreview(item.productId) }
        )
        rvCart.apply {
            adapter = this@CartActivity.adapter
            layoutManager = LinearLayoutManager(this@CartActivity)
        }
    }

    private fun observeCartItems() {
        lifecycleScope.launchWhenStarted {
            viewModel.cartItems.collect { list ->
                if (list.isEmpty()) {
                    showEmptyCart()
                } else {
                    showCart(list)
                }
            }
        }
    }

    private fun showEmptyCart() {
        rvCart.visibility = View.GONE
        tvEmptyCart.visibility = View.VISIBLE
        tvSubtotal.visibility = View.GONE
        tvTax.visibility = View.GONE
        tvGrandTotal.visibility = View.GONE
        btnCheckout.visibility = View.GONE
    }

    private fun showCart(list: List<CartItem>) {
        rvCart.visibility = View.VISIBLE
        tvEmptyCart.visibility = View.GONE
        btnCheckout.visibility = View.VISIBLE
        tvSubtotal.visibility = View.VISIBLE
        tvTax.visibility = View.VISIBLE
        tvGrandTotal.visibility = View.VISIBLE

        adapter.updateList(list)

        val (subtotal, tax, grandTotal) = viewModel.getTotals()
        tvSubtotal.text = "Subtotal: $${"%.2f".format(subtotal)}"
        tvTax.text = "Tax: $${"%.2f".format(tax)}"
        tvGrandTotal.text = "Grand Total: $${"%.2f".format(grandTotal)}"
    }

    private fun setupCheckout() {
        btnCheckout.setOnClickListener {
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
            viewModel.clearCart()
        }
    }

    private fun openPreview(productId: Int) {
        val intent = Intent(this, PreviewActivity::class.java).apply {
            putExtra("product_id", productId)
        }
        startActivity(intent)
    }
}

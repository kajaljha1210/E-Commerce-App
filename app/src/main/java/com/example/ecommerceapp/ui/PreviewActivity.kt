package com.example.ecommerceapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.remote.Product
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {

    private val viewModel: ProductViewModel by viewModels()

    private lateinit var toolbar: MaterialToolbar
    private lateinit var imgProduct: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductDescription: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvQuantity: TextView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTax: TextView
    private lateinit var tvGrandTotal: TextView
    private lateinit var btnPlaceOrder: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        initViews()
        setupToolbar()

        val productId = intent.getIntExtra("product_id", -1)
        if (productId == -1) return

        loadProduct(productId)

        btnPlaceOrder.setOnClickListener {
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        imgProduct = findViewById(R.id.imgProduct)
        tvProductName = findViewById(R.id.tvProductName)
        tvProductDescription = findViewById(R.id.tvProductDes)
        tvPrice = findViewById(R.id.tvPrice)
        tvQuantity = findViewById(R.id.tvQuantity)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvTax = findViewById(R.id.tvTax)
        tvGrandTotal = findViewById(R.id.tvGrandTotal)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun loadProduct(productId: Int) {
        lifecycleScope.launchWhenStarted {
            val product = viewModel.getProductById(productId) ?: return@launchWhenStarted
            displayProductDetails(product)
        }
    }

    private fun displayProductDetails(product: Product) {
        tvProductName.text = product.name
        supportActionBar?.title = product.name
        tvPrice.text = "Price: $${"%.2f".format(product.price)}"
        tvProductDescription.text = product.description

        val quantity = 1
        tvQuantity.text = "Quantity: $quantity"

        val subtotal = calculateSubtotal(product.price, quantity)
        val tax = calculateTax(subtotal)
        val grandTotal = subtotal + tax

        tvSubtotal.text = "Subtotal: $${"%.2f".format(subtotal)}"
        tvTax.text = "Tax (10%): $${"%.2f".format(tax)}"
        tvGrandTotal.text = "Grand Total: $${"%.2f".format(grandTotal)}"

        setProductImage(product.imageUrl)
    }

    private fun calculateSubtotal(price: Double, quantity: Int) = price * quantity

    private fun calculateTax(subtotal: Double) = subtotal * 0.1

    private fun setProductImage(imageName: String) {
        val resId = resources.getIdentifier(imageName, "drawable", packageName)
        imgProduct.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)
    }
}

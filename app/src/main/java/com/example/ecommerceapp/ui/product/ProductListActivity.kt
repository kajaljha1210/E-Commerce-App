package com.example.ecommerceapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.UserRepository
import com.example.ecommerceapp.data.local.remote.CartItem
import com.example.ecommerceapp.data.local.remote.Product
import com.example.ecommerceapp.ui.cart.CartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.jvm.java
@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    private lateinit var rvProducts: RecyclerView
    private lateinit var ivCart: ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        initViews()
        setupToolbar()
        setupRecyclerView()
        setupCartClick()
        observeProducts()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        rvProducts = findViewById(R.id.rvProducts)
        ivCart = findViewById(R.id.ivCart)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Products"
            setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            emptyList(),
            onAddToCart = { product -> addProductToCart(product) },
            onItemClick = { product -> openPreview(product) }
        )

        rvProducts.apply {
            adapter = this@ProductListActivity.adapter
            layoutManager = LinearLayoutManager(this@ProductListActivity)
        }
    }

    private fun setupCartClick() {
        ivCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun observeProducts() {
        lifecycleScope.launchWhenStarted {
            viewModel.products.collect { list ->
                adapter.updateList(list)
            }
        }
    }

    private fun addProductToCart(product: Product) {
        lifecycleScope.launch {
            viewModel.addToCart(product)
            Toast.makeText(
                this@ProductListActivity,
                "${product.name} added to cart",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPreview(product: Product) {
        val intent = Intent(this, PreviewActivity::class.java).apply {
            putExtra("product_id", product.id)
        }
        startActivity(intent)
    }
}

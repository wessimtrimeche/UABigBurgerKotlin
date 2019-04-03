package com.example.uabigburgerkotlin.module.productslist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.data.remote.dto.ECatalogProduct
import com.example.uabigburgerkotlin.module.adapter.ProductsAdapter
import com.example.uabigburgerkotlin.module.adapter.ProductsAdapter.ProductClickListener
import com.example.uabigburgerkotlin.module.productbasket.ProductBasketActivity
import com.example.uabigburgerkotlin.module.productdetails.ProductDetailsActivity
import com.google.gson.Gson
import io.reactivex.disposables.Disposable

class ProductsListActivity : AppCompatActivity(), ProductsListView {


    private lateinit var presenter: ProductsListPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorText: TextView
    private lateinit var retryButton: Button
    private lateinit var errorImage: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var myToolbar: Toolbar
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var productsAdapter: ProductsAdapter
    private var catalogProductsList = mutableListOf<ECatalogProduct>()

    override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)
        initViews()
        setSupportActionBar(myToolbar)
        presenter = ProductsListPresenter(this)
        presenter.getFullProducts()
        recyclerView.setLayoutManager(gridLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        productsAdapter = ProductsAdapter(object : ProductClickListener {
            override fun onProductClicked(catalogProductModel: ECatalogProduct) {
                val intent = Intent(getApplicationContext(), ProductDetailsActivity::class.java)
                intent.putExtra(ProductDetailsActivity.catalogProductKey, Gson().toJson(catalogProductModel))
                startActivity(intent)

            }


        })
        recyclerView.setAdapter(productsAdapter)
        retryButton.setOnClickListener({ presenter.getFullProducts() })
    }

    fun initViews() {
        myToolbar = findViewById(R.id.my_toolbar)
        progressBar = findViewById(R.id.progressBar)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.catalogRecyclerView)
        errorText = findViewById(R.id.errorLabel)
        retryButton = findViewById(R.id.retry)
        errorImage = findViewById(R.id.errorImage)
    }

    override fun showProgress() {
        progressBar.setVisibility(View.VISIBLE)
    }

    override fun hideProgress() {
        progressBar.setVisibility(View.GONE)
    }

    override fun onFetchCatalogProducts(catalogProducts: MutableList<ECatalogProduct>) {
        retryButton.setVisibility(View.GONE)
        errorText.setVisibility(View.GONE)
        errorImage.setVisibility(View.GONE)
        catalogProductsList.addAll(catalogProducts)
        productsAdapter.addItems(catalogProductsList)
    }

    override fun onFetchCatalogProductsFailed() {
        retryButton.setVisibility(View.VISIBLE)
        errorImage.setVisibility(View.VISIBLE)
        errorText.setVisibility(View.VISIBLE)
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.basket -> {
                val intent = Intent(getApplicationContext(), ProductBasketActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.reset).setVisible(false)
        return true
    }
}
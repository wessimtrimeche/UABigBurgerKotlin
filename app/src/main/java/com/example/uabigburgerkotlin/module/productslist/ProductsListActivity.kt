package com.example.uabigburgerkotlin.module.productslist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
import com.example.uabigburgerkotlin.module.adapter.ProductsAdapter
import com.example.uabigburgerkotlin.module.adapter.ProductsAdapter.ProductClickListener
import com.example.uabigburgerkotlin.module.productbasket.ProductBasketActivity
import com.example.uabigburgerkotlin.module.productdetails.ProductDetailsActivity
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_products_list.*

class ProductsListActivity : AppCompatActivity(), ProductsListView {


    private lateinit var presenter: ProductsListPresenter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var productsAdapter: ProductsAdapter
    private var catalogProductsList = mutableListOf<CatalogProductModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)
        gridLayoutManager = GridLayoutManager(this, 2)
        setSupportActionBar(product_list_toolbar)
        presenter = ProductsListPresenter(this)
        presenter.getFullProducts()
        product_list_catalog_recycler_view.layoutManager = gridLayoutManager as RecyclerView.LayoutManager?
        product_list_catalog_recycler_view.itemAnimator = DefaultItemAnimator()
        productsAdapter = ProductsAdapter(object : ProductClickListener {
            override fun onProductClicked(catalogProductModel: CatalogProductModel) {
                val intent = Intent(applicationContext, ProductDetailsActivity::class.java)
                intent.putExtra(ProductDetailsActivity.catalogProductKey, Gson().toJson(catalogProductModel))
                startActivity(intent)

            }


        })
        product_list_catalog_recycler_view.adapter = productsAdapter
        product_list_retry.setOnClickListener { presenter.getFullProducts() }
    }

    override fun showProgress() {
        product_list_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        product_list_progress_bar.visibility = View.GONE
    }

    override fun onFetchCatalogProducts(catalogProducts: MutableList<CatalogProductModel>) {
        product_list_retry.visibility = View.GONE
        product_list_error_label.visibility = View.GONE
        product_list_error_image.visibility = View.GONE
        catalogProductsList.addAll(catalogProducts)
        productsAdapter.addItems(catalogProductsList)
    }

    override fun onFetchCatalogProductsFailed() {
        product_list_retry.visibility = View.VISIBLE
        product_list_error_label.visibility = View.VISIBLE
        product_list_error_image.visibility = View.VISIBLE
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.basket -> {
                val intent = Intent(applicationContext, ProductBasketActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.reset).isVisible = false
        return true
    }
}
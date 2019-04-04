package com.example.uabigburgerkotlin.module.productbasket

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.local.model.Product
import com.example.uabigburgerkotlin.data.provider.SharedPreferencesProvider
import com.example.uabigburgerkotlin.module.adapter.BasketAdapter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_product_basket.*
import kotlinx.android.synthetic.main.popup_confirmation.*
import timber.log.Timber
import javax.inject.Inject

class ProductBasketActivity : AppCompatActivity(), ProductBasketView {

    @Inject
    lateinit var preferences: SharedPreferencesProvider
    private lateinit var productBasketPresenter: ProductBasketPresenter
    private lateinit var productsAdapter: BasketAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var clickedProduct: Product
    private lateinit var gridLayoutManager: GridLayoutManager
    private val productArrayList = mutableListOf<Product>()
    private var totalBasketPrice = 0f
    private var sizeBasketItems: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_basket)

        UABigBurgerKotlinApp.uaBigBurgerAppComponent.inject(this)
        gridLayoutManager = GridLayoutManager(this, 2)
        bottomSheetBehavior = BottomSheetBehavior.from(popup_bottom_sheet)
        setSupportActionBar(product_basket_toolbar)
        productBasketPresenter = ProductBasketPresenter(this@ProductBasketActivity)
        productBasketPresenter.getBasketProducts()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        product_basket_recycler_view.layoutManager = gridLayoutManager
        product_basket_recycler_view.itemAnimator = DefaultItemAnimator()
        productsAdapter = BasketAdapter(object : BasketAdapter.BasketProductClickListener {
            override fun onBasketProductClicked(basketProductModel: Product) {
                clickedProduct = basketProductModel
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            }
        })

        product_basket_recycler_view.adapter = productsAdapter
        popup_title.setText(R.string.confirm_delete_popup)
        popup_confirm.setOnClickListener {
            preferences.putBoolean((clickedProduct.ref.toString()), false)
            productBasketPresenter.removeProduct(clickedProduct)
            totalBasketPrice -= clickedProduct.price
            product_basket_total_price.text = resources.getString(R.string.total_price, totalBasketPrice)
            sizeBasketItems = productsAdapter.removeItem(clickedProduct)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
        popup_cancel.setOnClickListener { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) }
    }


    override fun showProgress() {
        product_basket_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        product_basket_progress_bar.visibility = View.GONE
    }

    override fun onFetchBasketProducts(basketProducts: List<Product>) {
        if (basketProducts.isNotEmpty()) {
            product_basket_empty_cart.visibility = View.GONE
            product_basket_empty_cart_text.visibility = View.GONE
        } else {
            product_basket_total_price.visibility = View.GONE
        }
        productArrayList.addAll(basketProducts)
        productsAdapter.addItems(productArrayList)
        for (p in basketProducts) {
            totalBasketPrice += p.price
        }
        product_basket_total_price.text = resources.getString(R.string.total_price, totalBasketPrice)
    }

    override fun onFetchBasketProductsFailure() {
        Timber.e("Failed to fetch basket products ")
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onRemoveSuccess() {
        if (sizeBasketItems == 0) {
            product_basket_total_price.visibility = View.GONE
            product_basket_empty_cart.visibility = View.VISIBLE
            product_basket_empty_cart_text.visibility = View.VISIBLE
        }
        Toast.makeText(applicationContext, R.string.removal_success, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.basket -> true
            R.id.reset -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.basket).isVisible = false
        return true
    }
}
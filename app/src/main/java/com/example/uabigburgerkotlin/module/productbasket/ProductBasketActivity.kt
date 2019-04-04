package com.example.uabigburgerkotlin.module.productbasket

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.local.model.Product
import com.example.uabigburgerkotlin.data.provider.SharedPreferencesProvider
import com.example.uabigburgerkotlin.module.adapter.BasketAdapter
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class ProductBasketActivity : AppCompatActivity(), ProductBasketView {

    @Inject
    lateinit var preferences: SharedPreferencesProvider
    private lateinit var productBasketPresenter: ProductBasketPresenter
    private lateinit var productBasketToolbar: Toolbar
    private lateinit var productBasketProgressBar: ProgressBar
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var productsAdapter: BasketAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var clickedProduct: Product
    private lateinit var emptyCart: ImageView
    private lateinit var emptyCartText: TextView
    private lateinit var cancelAddToBasket: TextView
    private lateinit var confirmAddToBasket: TextView
    private lateinit var popupConfirmAddToBasket: TextView
    private lateinit var totalBasketPriceTv: TextView
    private val productArrayList = mutableListOf<Product>()
    private var totalBasketPrice = 0f
    private var sizeBasketItems: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_basket)

        UABigBurgerKotlinApp.uaBigBurgerAppComponent.inject(this)

        initViews()
        setSupportActionBar(productBasketToolbar)
        productBasketPresenter = ProductBasketPresenter(this@ProductBasketActivity)
        productBasketPresenter.getBasketProducts()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        productsAdapter = BasketAdapter(object : BasketAdapter.BasketProductClickListener {
            override fun onBasketProductClicked(basketProductModel: Product) {
                clickedProduct = basketProductModel
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            }
        })

        recyclerView.adapter = productsAdapter
        popupConfirmAddToBasket.setText(R.string.confirm_delete_popup)
        confirmAddToBasket.setOnClickListener {
            preferences.putBoolean((clickedProduct.ref.toString()), false)
            productBasketPresenter.removeProduct(clickedProduct)
            totalBasketPrice -= clickedProduct.price
            totalBasketPriceTv.text = resources.getString(R.string.total_price, totalBasketPrice)
            sizeBasketItems = productsAdapter.removeItem(clickedProduct)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
        cancelAddToBasket.setOnClickListener { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) }
    }

    private fun initViews() {
        productBasketToolbar = findViewById(R.id.product_basket_toolbar)
        productBasketProgressBar = findViewById(R.id.product_basket_progress_bar)
        totalBasketPriceTv = findViewById(R.id.product_basket_total_price)
        emptyCart = findViewById(R.id.product_basket_empty_cart)
        emptyCartText = findViewById(R.id.product_basket_empty_cart_text)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.basket_recycler_view)
        val llBottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        cancelAddToBasket = llBottomSheet.findViewById(R.id.cancel)
        confirmAddToBasket = llBottomSheet.findViewById(R.id.confirmation)
        popupConfirmAddToBasket = llBottomSheet.findViewById(R.id.popupConfirm)
    }

    override fun showProgress() {
        productBasketProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        productBasketProgressBar.visibility = View.GONE
    }

    override fun onFetchBasketProducts(basketProducts: List<Product>) {
        if (basketProducts.isNotEmpty()) {
            emptyCart.visibility = View.GONE
            emptyCartText.visibility = View.GONE
        } else {
            totalBasketPriceTv.visibility = View.GONE
        }
        productArrayList.addAll(basketProducts)
        productsAdapter.addItems(productArrayList)
        for (p in basketProducts) {
            totalBasketPrice += p.price
        }
        totalBasketPriceTv.text = resources.getString(R.string.total_price, totalBasketPrice)
    }

    override fun onFetchBasketProductsFailure() {
        Timber.e("Failed to fetch basket products ")
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onRemoveSuccess() {
        if (sizeBasketItems == 0) {
            totalBasketPriceTv.visibility = View.GONE
            emptyCart.visibility = View.VISIBLE
            emptyCartText.visibility = View.VISIBLE
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
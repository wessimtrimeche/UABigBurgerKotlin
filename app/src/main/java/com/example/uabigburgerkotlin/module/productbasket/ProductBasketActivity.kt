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
import com.example.uabigburgerkotlin.module.adapter.BasketAdapter
import io.reactivex.disposables.Disposable
import timber.log.Timber

class ProductBasketActivity : AppCompatActivity(), ProductBasketView {

    private lateinit var presenter: ProductBasketPresenter
    private lateinit var myToolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var productsAdapter: BasketAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var clickedProduct: Product
    private lateinit var emptyCart: ImageView
    private lateinit var emptyCartText: TextView
    private lateinit var cancel: TextView
    private lateinit var confirm: TextView
    private lateinit var popupConfirm: TextView
    private lateinit var totalPrice: TextView
    private val productArrayList = mutableListOf<Product>()
    private var total = 0f
    private var size: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_basket)
        initViews()
        setSupportActionBar(myToolbar)
        presenter = ProductBasketPresenter(this@ProductBasketActivity)
        presenter.getBasketProducts()
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
        popupConfirm.setText(R.string.confirm_delete_popup)
        confirm.setOnClickListener {
            UABigBurgerKotlinApp.preferences.putBoolean((clickedProduct.ref.toString()), false)
            presenter.removeProduct(clickedProduct)
            total -= clickedProduct.price
            totalPrice.text = resources.getString(R.string.total_price, total)
            size = productsAdapter.removeItem(clickedProduct)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
        cancel.setOnClickListener { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) }
    }

    private fun initViews() {
        myToolbar = findViewById(R.id.my_toolbar)
        progressBar = findViewById(R.id.progressBar)
        totalPrice = findViewById(R.id.totalPrice)
        emptyCart = findViewById(R.id.emptyCart)
        emptyCartText = findViewById(R.id.emptyCartText)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.basketRecyclerView)
        val llBottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        cancel = llBottomSheet.findViewById(R.id.cancel)
        confirm = llBottomSheet.findViewById(R.id.confirmation)
        popupConfirm = llBottomSheet.findViewById(R.id.popupConfirm)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun onFetchBasketProducts(basketProducts: List<Product>) {
        if (basketProducts.isNotEmpty()) {
            emptyCart.visibility = View.GONE
            emptyCartText.visibility = View.GONE
        } else {
            totalPrice.visibility = View.GONE
        }
        productArrayList.addAll(basketProducts)
        productsAdapter.addItems(productArrayList)
        for (p in basketProducts) {
            total += p.price
        }
        totalPrice.text = resources.getString(R.string.total_price, total)
    }

    override fun onFetchBasketProductsFailure() {
        Timber.e("Failed to fetch basket products ")
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onRemoveSuccess() {
        if (size == 0) {
            totalPrice.visibility = View.GONE
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
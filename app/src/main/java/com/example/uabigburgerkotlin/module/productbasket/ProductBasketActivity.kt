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

class ProductBasketActivity : AppCompatActivity(), ProductBasketView {
    //TODO: use more significant naming convention
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
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        recyclerView.setLayoutManager(gridLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        productsAdapter = BasketAdapter(object : BasketAdapter.BasketProductClickListener {
            override fun onBasketProductClicked(basketProductModel: Product) {
                clickedProduct = basketProductModel
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)

            }
        })

        recyclerView.setAdapter(productsAdapter)
        popupConfirm.setText(R.string.confirm_delete_popup)
        confirm.setOnClickListener({
            UABigBurgerKotlinApp.preferences.putBoolean((clickedProduct.ref.toString()), false)
            presenter.removeProduct(clickedProduct)
            total -= clickedProduct.price
            totalPrice.setText(getResources().getString(R.string.total_price, total))
            size = productsAdapter.removeItem(clickedProduct)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        })
        cancel.setOnClickListener({ bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) })
    }

    fun initViews() {
        myToolbar = findViewById(R.id.my_toolbar)
        progressBar = findViewById(R.id.progressBar)
        totalPrice = findViewById(R.id.totalPrice)
        emptyCart = findViewById(R.id.emptyCart)
        emptyCartText = findViewById(R.id.emptyCartText)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.basketRecyclerView)
        val llBottomSheet = findViewById(R.id.bottom_sheet) as LinearLayout
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        cancel = llBottomSheet.findViewById(R.id.cancel)
        confirm = llBottomSheet.findViewById(R.id.confirmation)
        popupConfirm = llBottomSheet.findViewById(R.id.popupConfirm)
    }

    override fun showProgress() {
        progressBar.setVisibility(View.VISIBLE)
    }

    override fun hideProgress() {
        progressBar.setVisibility(View.GONE)
    }

    override fun onFetchBasketProducts(basketProducts: List<Product>) {
        if (basketProducts.size > 0) {
            emptyCart.setVisibility(View.GONE)
            emptyCartText.setVisibility(View.GONE)
        } else {
            totalPrice.setVisibility(View.GONE)
        }
        productArrayList.addAll(basketProducts)
        productsAdapter.addItems(productArrayList)
        for (p in basketProducts) {
            total += p.price
        }
        totalPrice.setText(getResources().getString(R.string.total_price, total))
    }

    override fun onFetchBasketProductsFailure() {
        // TODO : never keep method without implementation. in case of no logic behind use Timber to just display text on console
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onRemoveSuccess() {
        if (size == 0) {
            totalPrice.setVisibility(View.GONE)
            emptyCart.setVisibility(View.VISIBLE)
            emptyCartText.setVisibility(View.VISIBLE)
        }
        Toast.makeText(getApplicationContext(), R.string.removal_success, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.basket -> return true
            R.id.reset -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.basket).setVisible(false)
        return true
    }
}
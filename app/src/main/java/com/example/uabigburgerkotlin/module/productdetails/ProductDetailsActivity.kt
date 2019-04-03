package com.example.uabigburgerkotlin.module.productdetails

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.local.model.Product
import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
import com.example.uabigburgerkotlin.module.productbasket.ProductBasketActivity
import com.google.gson.Gson
import io.reactivex.disposables.Disposable

class ProductDetailsActivity : AppCompatActivity(), ProductDetailsView {
    private lateinit var catalogProduct: CatalogProductModel
    private lateinit var price: TextView
    private lateinit var description: TextView
    private lateinit var cancel: TextView
    private lateinit var confirm: TextView
    private lateinit var popupConfirm: TextView
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var presenter: ProductDetailsPresenter
    private lateinit var progressBar: ProgressBar
    private var isAddedToBaset: Boolean = false
    private lateinit var myToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        initViews()
        setSupportActionBar(myToolbar)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        presenter = ProductDetailsPresenter(this)
        if (intent.extras != null) {
            catalogProduct =
                Gson().fromJson(intent?.getExtras()?.getString(catalogProductKey), CatalogProductModel::class.java)
            title = catalogProduct.title
            isAddedToBaset = UABigBurgerKotlinApp.preferences.getBoolean(catalogProduct.ref, false)
            price.text = String.format(getResources().getString(R.string.format), catalogProduct.price)
            description.text = catalogProduct.description
            Glide.with(applicationContext)
                .load(catalogProduct.thumbnail)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                )
                .into(imageView)
            if (isAddedToBaset) {
                button.setText(R.string.remove_from_basket)
                popupConfirm.setText(R.string.confirm_delete_popup)
            } else {
                button.setText(R.string.add_to_basket)
                popupConfirm.setText(R.string.confirm_add_popup)
            }
            button.setOnClickListener { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED) }
            cancel.setOnClickListener { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) }
            confirm.setOnClickListener {
                val product = Product(
                    Integer.parseInt(catalogProduct.ref), catalogProduct.title,
                    catalogProduct.description, catalogProduct.thumbnail, catalogProduct.price!!
                )
                if (!isAddedToBaset) {
                    presenter.addProduct(product)
                    UABigBurgerKotlinApp.preferences.putBoolean(catalogProduct.ref, true)
                } else {
                    presenter.removeProduct(product)
                    UABigBurgerKotlinApp.preferences.putBoolean(catalogProduct.ref, false)
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            }
        }
    }

    private fun initViews() {
        myToolbar = findViewById(R.id.my_toolbar)
        progressBar = findViewById(R.id.progressBar)
        price = findViewById(R.id.price)
        description = findViewById(R.id.description)
        imageView = findViewById(R.id.thumbnail)
        button = findViewById(R.id.button)
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

    override fun onProductAdded() {
        button.setText(R.string.remove_from_basket)
        popupConfirm.setText(R.string.confirm_delete_popup)
        isAddedToBaset = true
        Toast.makeText(applicationContext, R.string.adding_to_basket_success, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onRemoveSuccess() {
        button.setText(R.string.add_to_basket)
        popupConfirm.setText(R.string.confirm_add_popup)
        isAddedToBaset = false
        Toast.makeText(applicationContext, "Product successfully removed!", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.basket -> {
                val intent = Intent(applicationContext, ProductBasketActivity::class.java)
                startActivity(intent)
                true
            }
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

    companion object {
        const val catalogProductKey = "CATALOG_PRODUCT"
    }
}
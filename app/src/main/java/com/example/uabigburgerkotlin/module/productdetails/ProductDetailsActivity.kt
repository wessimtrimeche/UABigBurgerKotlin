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
    override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        initViews()
        setSupportActionBar(myToolbar)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        presenter = ProductDetailsPresenter(this)
        if (getIntent().getExtras() != null) {
            catalogProduct =
                Gson().fromJson(getIntent()?.getExtras()?.getString(catalogProductKey), CatalogProductModel::class.java)
            setTitle(catalogProduct.title)
            isAddedToBaset = UABigBurgerKotlinApp.preferences.getBoolean(catalogProduct.ref, false)
            price.setText(String.format(getResources().getString(R.string.format), catalogProduct.price))
            description.setText(catalogProduct.description)
            Glide.with(getApplicationContext())
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
            button.setOnClickListener({ bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED) })
            cancel.setOnClickListener({ bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) })
            confirm.setOnClickListener({
                val product = Product(
                    Integer.parseInt(catalogProduct.ref), catalogProduct.title,
                    catalogProduct.description, catalogProduct.thumbnail, catalogProduct.price
                )
                if (!isAddedToBaset) {
                    presenter.addProduct(product)
                    UABigBurgerKotlinApp.preferences.putBoolean(catalogProduct.ref, true)
                } else {
                    presenter.removeProduct(product)
                    UABigBurgerKotlinApp.preferences.putBoolean(catalogProduct.ref, false)
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            })
        }
    }

    fun initViews() {
        myToolbar = findViewById(R.id.my_toolbar)
        progressBar = findViewById(R.id.progressBar)
        price = findViewById(R.id.price)
        description = findViewById(R.id.description)
        imageView = findViewById(R.id.thumbnail)
        button = findViewById(R.id.button)
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

    override fun onProductAdded() {
        button.setText(R.string.remove_from_basket)
        popupConfirm.setText(R.string.confirm_delete_popup)
        isAddedToBaset = true
        Toast.makeText(getApplicationContext(), R.string.adding_to_basket_success, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onRemoveSuccess() {
        button.setText(R.string.add_to_basket)
        popupConfirm.setText(R.string.confirm_add_popup)
        isAddedToBaset = false
        Toast.makeText(getApplicationContext(), "Product successfully removed!", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.basket -> {
                val intent = Intent(getApplicationContext(), ProductBasketActivity::class.java)
                startActivity(intent)
                return true
            }
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

    companion object {
        val catalogProductKey = "CATALOG_PRODUCT"
    }
}
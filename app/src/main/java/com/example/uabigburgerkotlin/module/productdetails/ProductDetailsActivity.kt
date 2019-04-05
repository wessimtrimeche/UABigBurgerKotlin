package com.example.uabigburgerkotlin.module.productdetails

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.local.model.Product
import com.example.uabigburgerkotlin.data.local.provider.SharedPreferencesProvider
import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
import com.example.uabigburgerkotlin.module.productbasket.ProductBasketActivity
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.popup_confirmation.*
import javax.inject.Inject

class ProductDetailsActivity : AppCompatActivity(), ProductDetailsView {

    @Inject
    lateinit var preferences: SharedPreferencesProvider
    private lateinit var catalogProduct: CatalogProductModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var presenter: ProductDetailsPresenter
    private var isAddedToBaset: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        UABigBurgerKotlinApp.uaBigBurgerAppComponent.inject(this)

        bottomSheetBehavior = BottomSheetBehavior.from(popup_bottom_sheet)
        setSupportActionBar(product_details_toolbar)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        presenter = ProductDetailsPresenter(this)
        if (intent.extras != null) {
            catalogProduct =
                Gson().fromJson(intent?.getExtras()?.getString(catalogProductKey), CatalogProductModel::class.java)
            title = catalogProduct.title
            isAddedToBaset = preferences.getBoolean(catalogProduct.ref, false)
            product_details_price.text = String.format(getResources().getString(R.string.format), catalogProduct.price)
            product_details_description.text = catalogProduct.description
            Glide.with(applicationContext)
                .load(catalogProduct.thumbnail)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                )
                .into(product_details_thumbnail)
            if (isAddedToBaset) {
                product_details_button.setText(R.string.remove_from_basket)
                popup_title.setText(R.string.confirm_delete_popup)
            } else {
                product_details_button.setText(R.string.add_to_basket)
                popup_title.setText(R.string.confirm_add_popup)
            }
            product_details_button.setOnClickListener { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED) }
            popup_cancel.setOnClickListener { bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) }
            popup_confirm.setOnClickListener {
                val product = Product(
                    Integer.parseInt(catalogProduct.ref), catalogProduct.title,
                    catalogProduct.description, catalogProduct.thumbnail, catalogProduct.price!!
                )
                if (!isAddedToBaset) {
                    presenter.addProduct(product)
                    preferences.putBoolean(catalogProduct.ref, true)
                } else {
                    presenter.removeProduct(product)
                    preferences.putBoolean(catalogProduct.ref, false)
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            }
        }
    }


    override fun showProgress() {
        product_details_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        product_details_progress_bar.visibility = View.GONE
    }

    override fun onProductAdded() {
        product_details_button.setText(R.string.remove_from_basket)
        popup_title.setText(R.string.confirm_delete_popup)
        isAddedToBaset = true
        Toast.makeText(applicationContext, R.string.adding_to_basket_success, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy(disposable: Disposable) {
        disposable.dispose()
    }

    override fun onRemoveSuccess() {
        product_details_button.setText(R.string.add_to_basket)
        popup_title.setText(R.string.confirm_add_popup)
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
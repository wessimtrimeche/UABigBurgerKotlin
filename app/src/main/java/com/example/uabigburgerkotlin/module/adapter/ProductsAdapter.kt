package com.example.uabigburgerkotlin.module.adapter

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
import com.example.uabigburgerkotlin.module.adapter.viewholder.ProductViewHolder

class ProductsAdapter(private val productClickListener: ProductClickListener) :
    RecyclerView.Adapter<ProductViewHolder>() {
    private val catalogProducts = mutableListOf<CatalogProductModel>()

    override fun getItemCount(): Int {
        return catalogProducts.size
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false)
        return ProductViewHolder(layout)
    }

    override fun onBindViewHolder(@NonNull productViewHolder: ProductViewHolder, i: Int) {
        val currentCatalogProduct = catalogProducts.get(i)
        productViewHolder.name.setText(currentCatalogProduct.title)
        productViewHolder.price.text = String.format(
            productViewHolder.price.context.getResources().getString(R.string.format),
            currentCatalogProduct.price
        )
        Glide.with(productViewHolder.imageView.context)
            .load(currentCatalogProduct.thumbnail)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
            )
            .into(productViewHolder.imageView)
        productViewHolder.cardView.setOnClickListener { productClickListener.onProductClicked(currentCatalogProduct) }
    }

    fun addItems(catalogProducts: MutableList<CatalogProductModel>) {
        this.catalogProducts.addAll(catalogProducts)
        notifyDataSetChanged()
    }

    interface ProductClickListener {
        fun onProductClicked(catalogProductModel: CatalogProductModel)
    }
}
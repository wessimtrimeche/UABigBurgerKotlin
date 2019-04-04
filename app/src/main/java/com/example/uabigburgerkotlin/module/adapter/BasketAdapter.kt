package com.example.uabigburgerkotlin.module.adapter

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.data.local.model.Product
import com.example.uabigburgerkotlin.module.adapter.viewholder.ProductViewHolder

class BasketAdapter(private val productClickListener: BasketProductClickListener) :
    RecyclerView.Adapter<ProductViewHolder>() {


    private val basketProducts = mutableListOf<Product>()


    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val layout = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_product, viewGroup, false)
        return ProductViewHolder(layout)
    }


    override fun getItemCount(): Int {
        return basketProducts.size
    }

    override fun onBindViewHolder(@NonNull productViewHolder: ProductViewHolder, i: Int) {
        val currentCatalogProduct = basketProducts[i]
        productViewHolder.name.text = currentCatalogProduct.title
        productViewHolder.price.text = String.format(
            productViewHolder.price.context.resources.getString(R.string.format),
            currentCatalogProduct.price
        )
        Glide.with(productViewHolder.cardView.context)
            .load(currentCatalogProduct.thumbnail)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
            )
            .into(productViewHolder.imageView)
        productViewHolder.cardView.setOnClickListener {
            productClickListener.onBasketProductClicked(
                currentCatalogProduct
            )
        }
    }

    fun addItems(basketProducts: MutableList<Product>) {
        this.basketProducts.addAll(basketProducts)
        notifyDataSetChanged()
    }

    fun removeItem(product: Product): Int {
        this.basketProducts.remove(product)
        notifyDataSetChanged()
        return this.basketProducts.size
    }

    interface BasketProductClickListener {
        fun onBasketProductClicked(basketProductModel: Product)
    }
}
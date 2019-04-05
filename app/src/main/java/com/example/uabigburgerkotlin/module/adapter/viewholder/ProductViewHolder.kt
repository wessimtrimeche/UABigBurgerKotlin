package com.example.uabigburgerkotlin.module.adapter.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_product.view.*

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.item_product_title
    var price: TextView = view.item_product_price
    var imageView: ImageView = view.item_product_thumbnail
    var cardView: CardView = view.item_product_card_view

}
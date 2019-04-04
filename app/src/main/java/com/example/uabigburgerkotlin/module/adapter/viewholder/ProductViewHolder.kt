package com.example.uabigburgerkotlin.module.adapter.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.uabigburgerkotlin.R

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.findViewById(R.id.item_product_title)
    var price: TextView = view.findViewById(R.id.item_product_price)
    var imageView: ImageView = view.findViewById(R.id.item_product_thumbnail)
    var cardView: CardView = view.findViewById(R.id.item_product_card_view)

}
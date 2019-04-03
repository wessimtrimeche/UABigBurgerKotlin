package com.example.uabigburgerkotlin.module.adapter.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.uabigburgerkotlin.R

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView
    var price: TextView
    var imageView: ImageView
    var cardView: CardView

    init {
        name = view.findViewById(R.id.itemTitle)
        price = view.findViewById(R.id.itemPrice)
        imageView = view.findViewById(R.id.itemThumbnail)
        cardView = view.findViewById(R.id.cardView)
    }
}
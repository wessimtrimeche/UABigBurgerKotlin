package com.example.uabigburgerkotlin.module.adapter.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.uabigburgerkotlin.R

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.findViewById(R.id.itemTitle)
    var price: TextView = view.findViewById(R.id.itemPrice)
    var imageView: ImageView = view.findViewById(R.id.itemThumbnail)
    var cardView: CardView = view.findViewById(R.id.cardView)

}
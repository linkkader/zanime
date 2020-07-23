
package com.zanime.link.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zanime.link.R
import com.zanime.link.type.Anime


class AdapterItemSearch2(var context: Context?, var animes:List<Anime>) : RecyclerView.Adapter<AdapterItemSearch2.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.img)
        val img : ImageView = itemView.findViewById(R.id.img)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_top, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = animes[position].name
        Picasso.get().load(animes[position].img).error(R.drawable.vstream).into(holder.img)
    }
    override fun getItemCount() = animes.size
}



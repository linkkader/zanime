package com.zanime.link.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.zanime.link.R
import com.zanime.link.database.list.List1

class AdapterDouble(var context : Context?) : RecyclerView.Adapter<AdapterDouble.ViewHolder>() {
    var i = 0
    var lists = emptyList<List1>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster1 : RoundedImageView = itemView.findViewById(R.id.poster1)
        val poster2 : RoundedImageView = itemView.findViewById(R.id.poster2)
        val title1 = itemView.findViewById<TextView>(R.id.title1)
        val title2 = itemView.findViewById<TextView>(R.id.title2)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_double_top,parent,false))
    }
    override fun getItemCount() = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Picasso.get().load(lists[position].img).error(R.drawable.vstream).into(holder.poster1)
        holder.title1.text = lists[position].name

        /*if (lists[i+1].name!="linkkader"){
            Picasso.get().load(lists[i+1].img).error(R.drawable.vstream).into(holder.poster2)
            holder.title2.text = lists[i+1].name
        }
        i+=2

         */
    }
    internal fun set(lists : List<List1>){
        this.lists = lists
        notifyDataSetChanged()
    }
}

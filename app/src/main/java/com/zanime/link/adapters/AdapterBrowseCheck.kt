package com.zanime.link.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.squareup.picasso.Picasso
import com.zanime.link.R
import com.zanime.link.type.Source

class AdapterBrowseCheck(var context : Context?, var lists:List<Source>, var fragmentManager: FragmentManager) : RecyclerView.Adapter<AdapterBrowseCheck.ViewHolder>() {
    val sharedPreferences = context!!.getSharedPreferences("source",Context.MODE_PRIVATE)
    var str = sharedPreferences.getString("source","")
    val editor = sharedPreferences.edit()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img :ImageView = itemView.findViewById(R.id.source_img)
        val tilte : TextView = itemView.findViewById(R.id.source_name)
        val language : TextView = itemView.findViewById(R.id.language_name)
        val switch : SwitchMaterial = itemView.findViewById(R.id.materialswitch)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_source_check,parent,false))
    }
    override fun getItemCount() = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (str!!.contains("*${lists[position].sourceName}*")){
            holder.switch.isChecked = false
        }
        holder.language.text = lists[position].language
        Picasso.get().load(lists[position].img).error(R.drawable.vstream).into(holder.img)
        holder.tilte.text = lists[position].sourceName.toUpperCase()
        holder.tilte.setOnClickListener{
            str = ""
            editor.putString("source",str)
            editor.apply()
        }
        holder.switch.setOnClickListener{
            if(str!!.contains("*${lists[position].sourceName}*")){
                str = str!!.replace("*${lists[position].sourceName}*","")
                editor.putString("source",str)
                editor.apply()
            }else{
                str += "*"+ lists[position].sourceName+"*"
                editor.putString("source",str)
                editor.apply()
            }
        }
    }
}

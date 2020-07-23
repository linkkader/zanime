package com.zanime.link.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.zanime.link.R
import com.zanime.link.type.Season

class AdapterSeason(var context : Context?, var seasons: List<Season>, var img : String) : RecyclerView.Adapter<AdapterSeason.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img : RoundedImageView = itemView.findViewById(R.id.seasonPoster)
        val title : TextView = itemView.findViewById(R.id.season_title)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_season,parent,false))
    }
    override fun getItemCount(): Int {
        return  seasons.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = "Linkkader $position"
        Picasso.get().load(img).error(R.drawable.outwatchlist).into(holder.img)
        /*holder.recyclerCategory.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.recyclerCategory.setHasFixedSize(true)
        holder.recyclerCategory.adapter = AdapterRecyclerCategory(context!!)
        Picasso.get().load("https://cdn.voiranime.to/wp-content/uploads/2020/04/thumb_5ea8d88c2dc4a.jpg").into(holder.img)
        Picasso.get().load("https://cdn.voiranime.to/wp-content/uploads/2020/06/thumb_5eee57f7c1f70-193x278.jpg").into(holder.backgoundimg)
        holder.title.text = "THE GOD OF HIGH SCHOOL"
        if (holder.title.text.count()>20){
            holder.title.textSize = 20F
        }
        if (holder.title.text.count()>30){
            holder.title.textSize = 15F
        }
        if (holder.title.text.count()>40){
            holder.title.textSize = 14F
        }
        holder.native.text = "Native : "
        holder.nativeText.text = "THE GOD OF HIGH SCHOOL ゴッド・オブ・ハイスクール"
        holder.status.text = "Status : "
        holder.statuText.text = "Ongoing"
        holder.start.text = "Start Date : "
        holder.startText.text = "Jul 6, 2020"
        holder.end.text = "End Date : "
        holder.endText.text = "Unknown"
        holder.studios.text = "Studios : "
        holder.studiosText.text = " MAPPA"
        holder.description.text ="On suit un lycéen et ses amis qui prennent part à un tournoi épique dans lequel le gagnant verra tous ses vœux s’exaucer. Cependant, les participants découvrent très vite qu’une mystérieuse organisation semble manipuler cette compétition dans l’ombre…"
        holder.like.init(parent)
        holder.like.setOnClickListener{
        }

         */
    }
}

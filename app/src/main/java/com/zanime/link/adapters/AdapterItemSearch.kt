
package com.zanime.link.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.type.Anime
import com.zanime.link.ui.AnimeInfo
import com.zanime.link.ui.MainActivity


class AdapterItemSearch(var context: Context?,var animes:List<Anime>,var source :String) : RecyclerView.Adapter<AdapterItemSearch.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val img : RoundedImageView = itemView.findViewById(R.id.img)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_top, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = animes[position].name
        Picasso.get().load(animes[position].img).error(R.drawable.vstream).into(holder.img)
        holder.itemView.setOnClickListener {
            if (!verifyAvailableNetwork(context as AppCompatActivity)){
                Toast.makeText(context, (context as AppCompatActivity).getString(R.string.verify_internet),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            AnimeInfo.name = animes[position].name
            AnimeInfo.url = animes[position].link
            AnimeInfo.source = source
            context!!.startActivity(Intent(context, AnimeInfo::class.java))
        }
    }

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
    override fun getItemCount() = animes.size
}



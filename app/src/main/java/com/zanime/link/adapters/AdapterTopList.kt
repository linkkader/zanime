
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
import com.zanime.link.database.Top
import com.zanime.link.ui.AnimeInfo
import com.zanime.link.ui.MainActivity


class AdapterTopList(var context: Context?) : RecyclerView.Adapter<AdapterTopList.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tops = emptyList<Top>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster1 : RoundedImageView = itemView.findViewById(R.id.poster1)
        val title1 = itemView.findViewById<TextView>(R.id.title1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title1.text = tops[position].name
        Picasso.get().load(tops[position].img).error(R.drawable.vstream).into(holder.poster1)
        holder.itemView.setOnClickListener {
            if (!verifyAvailableNetwork(context as AppCompatActivity)){
                Toast.makeText(context, (context as AppCompatActivity).getString(R.string.verify_internet),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            AnimeInfo.name = tops[position].name
            AnimeInfo.url = tops[position].url
            AnimeInfo.source = tops[position].source
            context!!.startActivity(Intent(context, AnimeInfo::class.java))
        }
    }
    internal fun setWords(words: List<Top>) {
        this.tops = words
        notifyDataSetChanged()
    }
    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
    override fun getItemCount() = tops.size
}



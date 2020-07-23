package com.zanime.link.adapters

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.database.history.History
import com.zanime.link.type.Episode
import com.zanime.link.type.Server
import com.zanime.link.ui.AnimeInfo
import com.zanime.link.ui.MainActivity
import com.zanime.link.ui.VodActivity
import com.zanime.link.ui.fragment.FragmentAnimeInfo
import com.zanime.link.ui.fragment.FragmentEpisode
import java.util.*


class AdapterEpisode(var context : Context?, var episodes : List<Episode>, var animeInfo: com.zanime.link.type.AnimeInfo) : RecyclerView.Adapter<AdapterEpisode.ViewHolder>() {
    val sharedPreferences = context!!.getSharedPreferences(FragmentAnimeInfo.str,Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    var string = sharedPreferences.getString("episode","")
    var justserver = Server()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout : LinearLayout = itemView.findViewById(R.id.mainLayout)
        val episodeTitle = itemView.findViewById<TextView>(R.id.episodeTitle)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_episode,parent,false))
    }
    override fun getItemCount(): Int {
        return  episodes.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.episodeTitle.text = episodes[position].name
        if (string!!.contains("*$position*")){
            holder.episodeTitle.setTextColor(context!!.getColor(R.color.grey_black))
            holder.linearLayout.background = context!!.getDrawable(R.drawable.noback)
        }else{
            holder.episodeTitle.setTextColor(context!!.getColor(R.color.white))
            holder.linearLayout.background = context!!.getDrawable(R.drawable.noback)
        }
        holder.itemView.setOnClickListener {
            if (!verifyAvailableNetwork(context!!.applicationContext)){
                Toast.makeText(context, context!!.getString(R.string.verify_internet),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(AnimeInfo.fa,"inters")
            }
            FragmentEpisode.listViewModel.insert(
                History(animeInfo.name,animeInfo.img
                ,animeInfo.source,animeInfo.source+animeInfo.name
                , Date(Date().time).toString(),animeInfo.url)
            )
            VodActivity.sourceName = animeInfo.source
            VodActivity.position = position
            string = "$string*$position*"
            holder.episodeTitle.setTextColor(context!!.getColor(R.color.grey_black))
            holder.linearLayout.background = context!!.getDrawable(R.drawable.noback)
            editor.putString("episode",string)
            editor.putString("resume",episodes[position].name)
            editor.putInt("resumePosition", VodActivity.position)
            editor.apply()
            VodActivity.episodes = episodes
            AnimeInfo.fa.startActivity(Intent(context,VodActivity::class.java))
        }
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            holder.episodeTitle.setTextColor(context!!.getColor(R.color.white))
            holder.linearLayout.background = context!!.getDrawable(R.drawable.noback)
            string = string!!.replace("*$position*","")
            editor.putString("episode",string)
            editor.apply()
            return@OnLongClickListener true
        })
    }
    fun verifyAvailableNetwork(activity: Context):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}

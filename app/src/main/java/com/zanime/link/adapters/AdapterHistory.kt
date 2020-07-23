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
import com.loopeer.shadow.ShadowView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.database.history.History
import com.zanime.link.ui.AnimeInfo
import com.zanime.link.ui.MainActivity
import com.zanime.link.ui.fragment.FragmentHistory


class AdapterHistory(var context: Context?) : RecyclerView.Adapter<AdapterHistory.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var historys = emptyList<History>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster : RoundedImageView = itemView.findViewById(R.id.poster)
        val title = itemView.findViewById<TextView>(R.id.title)
        val last = itemView.findViewById<TextView>(R.id.last)
        val remove = itemView.findViewById<ShadowView>(R.id.remove)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = historys[position].name
        Picasso.get().load(historys[position].img).error(R.drawable.vstream).into(holder.poster)
        val sharedPreferences = context!!.getSharedPreferences(historys[position].source+historys[position].name,Context.MODE_PRIVATE)
        val lastTitle = sharedPreferences.getString("resume","")
        holder.last.text = lastTitle
        holder.itemView.setOnClickListener {
            if (!verifyAvailableNetwork(context as AppCompatActivity)){
                Toast.makeText(context, (context as AppCompatActivity).getString(R.string.verify_internet),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            AnimeInfo.name = historys[position].name
            AnimeInfo.url = historys[position].url
            AnimeInfo.source = historys[position].source
            context!!.startActivity(Intent(context, AnimeInfo::class.java))
        }
        holder.remove.setOnClickListener {
            FragmentHistory.listViewModel.remove(historys[position].source,historys[position].name)
        }
    }
    internal fun setHistorys(historys: List<History>) {
        this.historys = historys
        notifyDataSetChanged()
    }

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
    override fun getItemCount() = historys.size

}



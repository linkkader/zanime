package com.zanime.link.adapters

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.loopeer.shadow.ShadowView
import com.squareup.picasso.Picasso
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.type.Source
import com.zanime.link.ui.MainActivity
import com.zanime.link.ui.fragment.FragmentBrowserItem
import com.zanime.link.ui.fragment.FragmentGenre

class AdapterBrowse(var context : Context?, var lists:List<Source>, var fragmentManager: FragmentManager) : RecyclerView.Adapter<AdapterBrowse.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img :ImageView = itemView.findViewById(R.id.source_img)
        val tilte : TextView = itemView.findViewById(R.id.source_name)
        val genre : ShadowView = itemView.findViewById(R.id.genre_button)
        val browser : ShadowView = itemView.findViewById(R.id.browse)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_source,parent,false))
    }
    override fun getItemCount() = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(lists[position].img).error(R.drawable.vstream).into(holder.img)
        holder.tilte.text = lists[position].sourceName.toUpperCase()
        holder.genre.setOnClickListener{
            if (!verifyAvailableNetwork(context as AppCompatActivity)){
                Toast.makeText(context, (context as AppCompatActivity).getString(R.string.verify_internet),Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            fragmentManager.beginTransaction()
                .add(R.id.container_main, FragmentGenre(lists[position].sourceName))
                .addToBackStack(null)
                .commit()
        }
        holder.browser.setOnClickListener{
            if (!verifyAvailableNetwork(context as AppCompatActivity)){
                Toast.makeText(context, (context as AppCompatActivity).getString(R.string.verify_internet),Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            fragmentManager.beginTransaction()
                .add(R.id.container_main, FragmentBrowserItem(lists[position].sourceName))
                .addToBackStack(null)
                .commit()
        }
    }

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}

package com.zanime.link.adapters

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.type.Genre
import com.zanime.link.ui.MainActivity
import com.zanime.link.ui.fragment.FragmentDoubleList

class AdapterGenres(var context : Context?, var genres: List<Genre>, val fragmentManager: FragmentManager, val sourceName:String) : RecyclerView.Adapter<AdapterGenres.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val genre = itemView.findViewById<Button>(R.id.genre_button)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_genre,parent,false))
    }
    override fun getItemCount(): Int {
        return  genres.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.genre.text = genres[position].name
        holder.genre.setOnClickListener{
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            if (!verifyAvailableNetwork(context as AppCompatActivity)){
                Toast.makeText(context, (context as AppCompatActivity).getString(R.string.verify_internet),Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            fragmentManager.beginTransaction()
                .add(R.id.container_main, FragmentDoubleList(genres[position].url,sourceName,"genre"))
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

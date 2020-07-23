package com.zanime.link.adapters

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.type.Genre

class AdapterGenre(var context : Context?, var genres: List<Genre>) : RecyclerView.Adapter<AdapterGenre.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val genre = itemView.findViewById<Button>(R.id.category)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.category,parent,false))
    }
    override fun getItemCount(): Int {
        return  genres.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.genre.text = genres[position].name
    }

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}

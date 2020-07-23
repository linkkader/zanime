package com.zanime.link.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterBrowse
import com.zanime.link.type.Source
import java.util.*


class FragmentBrowser : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var list = listOf<Source>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insert("voiranime","https://cdn.voiranime.to/wp-content/uploads/fbrfg/apple-touch-icon.png")
        insert("gogoanime","https://cdn.gogocdn.net/files/gogo/img/favicon.png")
        insert("otakufr","https://www.otakufr.com/favicon.png")
        insert("vostfree","https://vostfree.com/templates/Animix/images/logo.png")
        insert("animedao","http://animedao.com/img/animedaofb.png")
        insert("ianime","https://www.ianimes.org/img/i-anime.png")
        insert("animefreak","https://alternative.app/data/original/AnimeFreakPFDgTr.png")
        insert("animefenix","https://www.animefenix.com/themes/animefenix-frans185/images/AveFenix.png")
        insert("jkanime","https://cdn.jkanime.net/assets/images/logo.png")
        insert("anyanime","https://ww3.anyanime.com/wp-content/uploads/2015/11/Anyanime-logo1.png")
        val recycler = view.findViewById<RecyclerView>(R.id.double_list)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = AdapterBrowse(context,list,activity!!.supportFragmentManager)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }
    fun insert(name:String,img:String){
        val sharedPreferences = context!!.getSharedPreferences("source", Context.MODE_PRIVATE)
        val str = sharedPreferences.getString("source","")
        val source  = Source(name,"")
        source.img = img
        if (!str!!.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))list = list.plus(source)
    }
}
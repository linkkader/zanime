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
import com.zanime.link.adapters.AdapterSearch
import com.zanime.link.type.Source
import java.util.*

class FragmentSearch(var query:String) : Fragment() {
    var list = listOf<Source>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context!!.getSharedPreferences("source", Context.MODE_PRIVATE)
        val str = sharedPreferences.getString("source","")

        if (!str!!.toLowerCase(Locale.ROOT).contains("voiranime"))list= list.plus(Source("voiranime","https://voiranime.to/page/linkkader/?s=linkquery&post_type=wp-manga".replace("linkquery",query)))
        if (!str.toLowerCase(Locale.ROOT).contains("gogoanime"))list= list.plus(Source("gogoanime","https://www2.gogoanime.video//search.html?keyword=linkquery&page=linkkader".replace("linkquery",query)))
        if (!str.toLowerCase(Locale.ROOT).contains("vostfree"))list= list.plus(Source("vostfree","https://vostfree.com/index.php?do=search&subaction=search&search_start=linkkader&story=linkquery".replace("linkquery",query)))
        if (!str.toLowerCase(Locale.ROOT).contains("otakufr"))list= list.plus(Source("otakufr","https://www.otakufr.com/anime-list/search/linkquery/name-az/linkkader/".replace("linkquery",query)))
        if (!str.toLowerCase(Locale.ROOT).contains("animedao"))list= list.plus(Source("animedao","https://animedao.com/search/?key=linkquery".replace("linkquery",query)))
        if (!str.toLowerCase(Locale.ROOT).contains("ianime"))list= list.plus(Source("ianime","https://www.ianimes.org/resultat+linkquery.html".replace("linkquery",query).replace("%20","+")))
        if (!str.toLowerCase(Locale.ROOT).contains("animefreak"))list= list.plus(Source("animefreak","https://www.animefreak.tv/search/topSearch?q=linkquery".replace("linkquery",query).replace("%20","+")))
        if (!str.toLowerCase(Locale.ROOT).contains("animefenix"))list= list.plus(Source("animefenix","https://www.animefenix.com/animes?q=linkquery&page=linkkader".replace("linkquery",query).replace("%20","+")))
        if (!str.toLowerCase(Locale.ROOT).contains("jkanime"))list= list.plus(Source("jkanime","https://jkanime.net/buscar/linkquery/linkkader/".replace("linkquery",query).replace("%20","+")))
        if (!str.toLowerCase(Locale.ROOT).contains("anyanime"))list= list.plus(Source("anyanime","https://ww8.anyanime.com/page/linkkader/?s=linkquery".replace("linkquery",query).replace("%20","+")))
        val recycler = view.findViewById<RecyclerView>(R.id.double_list)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = AdapterSearch(context,list, activity!!.supportFragmentManager)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_recyclerview, container, false)
    }
}
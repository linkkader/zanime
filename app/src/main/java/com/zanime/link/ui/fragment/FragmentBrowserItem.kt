package com.zanime.link.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterBrowserItem
import com.zanime.link.type.Item


class FragmentBrowserItem(val sourceName:String) : Fragment() {
    var list = listOf<Item>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (sourceName=="voiranime"){
            insert("En cours","https://voiranime.to/page/linkkader/")
            insert("VF","https://voiranime.to/page/linkkader/?filter=dubbed")
            insert("Vostfr","https://voiranime.to/page/linkkader/?filter=subbed")
        }else if (sourceName == "gogoanime"){
            insert("Recent Release","https://www19.gogoanime.io?page=linkkader")
            insert("Popular","https://www19.gogoanime.io/popular.html?page=linkkader")
            insert("New Season","https://www19.gogoanime.io/new-season.html?page=linkkader")
            insert("Movies","https://www19.gogoanime.io/anime-movies.html?page=linkkader")
        }else if (sourceName == "vostfree"){
            insert("Latest","https://vostfree.com/lastnews/page/linkkader/")
            insert("vf","https://vostfree.com/animes-vf/page/linkkader/")
            insert("Vostfr","https://vostfree.com/animes-vostfr/page/linkkader/")
            insert("Film","https://vostfree.com/films-vf-vostfr/page/linkkader/")
        }else if (sourceName == "otakufr"){
            insert("Populaire","https://www.otakufr.com/anime-list/all/any/most-popular/name-az/linkkader/")
            insert("Film","https://www.otakufr.com/anime-list/tag/Film/name-az/linkkader/")
            insert("Ecchi","https://www.otakufr.com/anime-list/category/Ecchi/name-az/linkkader/")
            insert("Comedie","https://www.otakufr.com/anime-list/category/Comedie/name-az/linkkader/")
        }else if (sourceName == "animedao"){
            insert("Ecchi","https://animedao.com/animelist/genre/ecchi")
            insert("Harem","https://animedao.com/animelist/genre/harem")
            insert("School","https://animedao.com/animelist/genre/school")
            insert("Isekai","https://animedao.com/animelist/genre/isekai")
        }else if (sourceName == "ianime"){
            insert("Top 30","https://www.ianimes.org/top_30.php")
            insert("Series","https://www.ianimes.org/series.php")
            insert("film","https://www.ianimes.org/films.php?liste=b1u3vv0lSorJk9Lex0tbKZEtbz8RlMC9")
        }else if (sourceName == "animefreak"){
            insert("All","https://www.animefreak.tv/home/types/page/linkkader")
            insert("OVA Serie","https://www.animefreak.tv/home/types/OVA%20Series/page/linkkader")
            insert("Movie","https://www.animefreak.tv/home/types/Movie/page/linkkader")
            insert("TV Serie","https://www.animefreak.tv/home/types/TV%20Series/page/linkkader")
         }else if (sourceName == "animefenix"){
            insert("Anime","https://www.animefenix.com/animes?page=linkkader")
            insert("Ecchi","https://www.animefenix.com/animes?genero[]=ecchi&page=linkkader")
            insert("Isekai","https://www.animefenix.com/animes?genero[]=harem?page=linkkader")
        }else if (sourceName == "jkanime"){
            insert("pelicula","https://ww3.anyanime.com/wp-content/uploads/2015/11/Anyanime-logo1.png")
            insert("Ova","https://jkanime.net/tipo/ova/linkkader/")
        }else if (sourceName == "anyanime"){
            insert("الانميات","https://ww8.anyanime.com/list_anime_online/page/linkkader/")
            insert("اكشن","https://ww8.anyanime.com/series_online_category/%d8%a7%d9%83%d8%b4%d9%86/page/linkkader/")
            insert("مدرسي","https://ww8.anyanime.com/series_online_category/%d9%85%d8%af%d8%b1%d8%b3%d9%8a/page/linkkader/")
        }
        val recycler = view.findViewById<RecyclerView>(R.id.double_list)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = AdapterBrowserItem(context,list, activity!!.supportFragmentManager,sourceName)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }
    fun insert(name:String,url:String){
        val item  = Item()
        item.name = name
        item.url = url
        list = list.plus(item)
    }
}
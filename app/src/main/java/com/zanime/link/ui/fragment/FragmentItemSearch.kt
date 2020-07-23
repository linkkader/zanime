package com.zanime.link.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterItemSearch
import com.zanime.link.type.Anime
import org.jsoup.Jsoup


class FragmentItemSearch(var pos : Int =0) : Fragment() {
    var i = 0
    var check = 0
    var position =1
    var url = "https://voiranime.to/page/linkkader/?s=on&post_type=wp-manga"
    lateinit var   adapter : AdapterItemSearch
    lateinit var recycler : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler= view.findViewById(R.id.double_list)
        recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recycler.setHasFixedSize(true)
        /*recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)&&check==0) {
                    position++
                    jsoupResponse(url.replace("linkkader","$position"),"voiranime","genre")
                }
            }
        })


         */
        jsoupResponse (url.replace("linkkader",position.toString()) ,"voiranime" , "search")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }
    private fun jsoupResponse (url : String, tag : String, genre:String){
        var animes = listOf<Anime>()
        check = 2
        val thread : Thread = Thread {
            if (tag=="voiranime"){
                if (genre=="search"){
                    val doc = Jsoup.connect(url).timeout(0).maxBodySize(0).get()
                    val elements = doc.select("div.c-tabs-item").select("div.row")
                    for (element in elements) {
                        var str = element.toString().substringAfter("href=\"")
                        val anime = Anime()
                        anime.name = str.substringAfter("title=\"").substringBefore("\"")
                        anime.link = str.substringBefore("\"")
                        str = str.substringAfter("src=\"")
                        if (str.contains("png")){
                            anime.img = str.substringBeforeLast("png").substringAfterLast(",")+"png"
                        }else{
                            anime.img = str.substringBeforeLast("jpg").substringAfterLast(",")+"jpg"
                        }
                        anime.img=anime.img.replace(" ","")
                        animes=animes.plus(anime)

                    }
                    check = 1
                }
            }
        }
        thread.start()
    }
    companion object {
        @JvmStatic
        fun newInstance(p:Int) =
            FragmentItemSearch(p).apply {
                arguments = Bundle().apply {}
            }
    }
}
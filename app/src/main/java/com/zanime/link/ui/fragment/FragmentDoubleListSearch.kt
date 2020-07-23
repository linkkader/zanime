package com.zanime.link.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterTopListSeach
import com.zanime.link.database.search.Top
import com.zanime.link.database.search.TopViewModel
import com.zanime.link.type.Anime
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception


class FragmentDoubleListSearch(var url : String, val source : String, var genre:String) : Fragment() {
    var i = 0
    var check = 0
    var position =1
    lateinit var progressBar: ProgressBar
    lateinit var listViewModel : TopViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {}
        val recycler : RecyclerView = view.findViewById(R.id.double_list)
        progressBar = view.findViewById(R.id.progress_bar)
        recycler.layoutManager = GridLayoutManager(context,2)
        val adapter = AdapterTopListSeach(context)
        recycler.adapter = adapter
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)&&check==0) {
                    position++
                    progressBar.visibility = View.VISIBLE
                    if (url.contains("linkkader"))jsoupResponse(url.replace("linkkader","$position"),source,genre)
                }
            }
        })
        listViewModel = ViewModelProvider(this@FragmentDoubleListSearch).get(TopViewModel(activity!!.application)::class.java)
        listViewModel.allWords.observe(viewLifecycleOwner , Observer { lists ->
            lists.let { adapter.setWords(it) }
        })
        listViewModel.deleteAll()
        jsoupResponse(url.replace("linkkader","$position"),source,genre)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_double_list, container, false)
    }
    fun jsoupResponse (url : String,tag : String,genre:String){
        val thread : Thread = Thread {
            check = 2
            try {

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
                            insert(anime)
                            i++
                        }
                        check=0
                    }
                    else if (genre=="genre") {
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements = doc.select("div.page-content-listing").select("div.page-item-detail")
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
                            insert(anime)
                            i++
                        }
                        check = 0
                    }
                    else{
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10)
                            .get()
                        val elements=doc.select("div.page-content-listing").select("div.page-item-detail")
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
                            insert(anime)
                        }
                        check = 0
                    }
                }
                else if (tag == "gogoanime") {
                    if (genre=="search"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements = doc.select("ul.items").select("li")
                        for (element in elements) {
                            var str = element.toString().substringAfter("href=\"")
                            val anime = Anime()
                            anime.name = str.substringAfter("title=\"").substringBefore("\"")
                            anime.link = str.substringBefore("\"")
                            str = str.substringAfter("src=\"")
                            anime.img = str.substringBefore("\"").replace(" ","%20")
                            listViewModel.insert(Top(anime.name,tag,anime.img,i.toString(),anime.link))
                            i++
                        }
                        check = 0
                    }
                    else {
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements = doc.select("ul.items").select("li")
                        for (element in elements) {
                            var str = element.toString().substringAfter("href=\"")
                            val anime = Anime()
                            anime.name = str.substringAfter("title=\"").substringBefore("\"")
                            anime.link = str.substringBefore("\"")
                            str = str.substringAfter("src=\"")
                            anime.img = str.substringBefore("\"").replace(" ","%20")
                            insert(anime)
                            i++
                        }
                        check = 0
                    }
                }
                else if (tag == "animedao") {
                    if (genre=="search"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements=doc.select("div.col-xs-12")
                        for(element in elements){
                            val str = element.toString()
                            if (str.contains("href")){
                                val anime = Anime()
                                anime.link = str.substringAfter("href=\"").substringBefore("\"")
                                anime.img = anime.link.replace("anime","images").substringBeforeLast("/").substringBefore("-dubbed")+".jpg"
                                anime.link = "https://animedao.com"+anime.link
                                anime.img = "https://animedao.com"+anime.img
                                anime.name = element.select("h4").select("b").text()
                                listViewModel.insert(Top(anime.name,tag,anime.img,i.toString(),anime.link))
                                i++
                            }
                        }
                        check = 0
                    }
                    else {
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements = doc.select("div.col-xs-6")
                        for (element in elements) {
                            var str = element.toString().substringAfter("href=\"")
                            val anime = Anime()
                            anime.link = str.substringAfter("href=\"").substringBefore("\"")
                            anime.img = anime.link.replace("anime","images").substringBeforeLast("/").substringBefore("-dubbed")+".jpg"
                            anime.link = "https://animedao.com"+anime.link
                            anime.img = "https://animedao.com"+anime.img
                            anime.name = element.text()
                            listViewModel.insert(Top(anime.name,tag,anime.img,i.toString(),anime.link))
                            i++
                        }
                        check = 0
                    }
                }
                else if (tag == "ianime") {
                    if (genre=="search"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements = doc.select("tbody")
                        for (element in elements) {
                            if (element.select("tbody").size>=2) continue

                            var str = element.toString()
                            val anime = Anime()
                            anime.name = str.substringAfter("<titre6>").substringBefore("<").replace("\n","").replace("  ","")
                            anime.img = str.substringAfter("url('").substringBefore("'")
                            anime.link = "https://www.ianimes.org/"+str.substringAfter("href=\"").substringBefore("\"")
                            if (anime.img.contains("jpg")) listViewModel.insert(Top(anime.name,tag,anime.img,i.toString(),anime.link))
                            i++
                        }
                        check = 0
                    }
                    else {
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements = doc.select("div.col-xs-6")
                        for (element in elements) {
                            var str = element.toString().substringAfter("href=\"")
                            val anime = Anime()
                            anime.link = str.substringAfter("href=\"").substringBefore("\"")
                            anime.img = anime.link.replace("anime","images").substringBeforeLast("/").substringBefore("-dubbed")+".jpg"
                            anime.link = "https://animedao.com"+anime.link
                            anime.img = "https://animedao.com"+anime.img
                            anime.name = element.text()
                            listViewModel.insert(Top(anime.name,tag,anime.img,i.toString(),anime.link))
                            i++
                        }
                        check = 0
                    }
                }
                else if (tag == "vostfree") {
                    if (genre=="genre"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        var elements=doc.getElementById("dle-content").select("div.movie-poster")
                        for (element in elements) {
                            var str = element.select("a").toString()
                            val anime = Anime()
                            anime.img = "https://vostfree.com"+element.toString().substringAfter("src=\"").substringBefore("\"")
                            anime.name = str.substringAfter("alt=\"").substringBefore("\"")
                            anime.link=str.substringAfter("short-poster").substringAfter("href=\"").substringBefore("\"")
                            insert(anime)
                            i++
                        }
                        check = 0
                    }
                    else if(genre=="search"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements=doc.select("div.search-result")
                        for (element in elements) {
                            val str = element.toString()
                            val anime = Anime()
                            anime.img = "https://vostfree.com"+element.toString().substringAfter("src=\"").substringBefore("\"")
                            anime.name = str.substringAfter("alt=\"").substringBefore("\"")
                            anime.link=str.substringAfter("short-poster").substringAfter("href=\"").substringBefore("\"")
                            insert(anime)
                            i++
                        }
                        check = 0
                    }
                    else{

                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        var elements=doc.getElementById("dle-content").select("div.movie-poster")
                        for (element in elements) {
                            var str = element.select("a").toString()
                            val anime = Anime()
                            anime.img = "https://vostfree.com"+element.toString().substringAfter("src=\"").substringBefore("\"")
                            anime.name = str.substringAfter("alt=\"").substringBefore("\"")
                            anime.link=str.substringAfter("short-poster").substringAfter("href=\"").substringBefore("\"")
                            insert(anime)
                            i++
                        }
                        check = 0
                    }
                }
                else if (tag == "otakufr") {
                    if (genre=="genre"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements=doc.select("div.lst").select("a")
                        var i2=0
                        while (i2<elements.size){
                            var str = elements[i2].toString()
                            val anime = Anime()
                            anime.name = elements[i2].text()
                            anime.link=str.substringAfter("short-poster").substringAfter("href=\"").substringBefore("\"")
                            if (i2+1==elements.size) break
                            str = elements[i2+1].toString()
                            anime.img = str.substringAfter("src=\"").substringBefore("\"")
                            insert(anime)
                            i2+=2
                            i++
                        }
                        check = 0
                    }else{
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements=doc.select("div.lst").select("a")
                        var i2=0
                        while (i2<elements.size){
                            var str = elements[i2].toString()
                            val anime = Anime()
                            anime.name = elements[i2].text()
                            anime.link=str.substringAfter("short-poster").substringAfter("href=\"").substringBefore("\"")
                            if (i2+1==elements.size) break
                            str = elements[i2+1].toString()
                            anime.img = str.substringAfter("src=\"").substringBefore("\"")
                            insert(anime)
                            i2+=2
                            i++
                        }
                        check = 0
                    }
                }
                else if (tag == "animefreak") {
                    if (genre=="genre"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements=doc.select("div.lst").select("a")
                        var i2=0
                        while (i2<elements.size){
                            var str = elements[i2].toString()
                            val anime = Anime()
                            anime.name = elements[i2].text()
                            anime.link=str.substringAfter("short-poster").substringAfter("href=\"").substringBefore("\"")
                            if (i2+1==elements.size) break
                            str = elements[i2+1].toString()
                            anime.img = str.substringAfter("src=\"").substringBefore("\"")
                            insert(anime)
                            i2+=2
                            i++
                        }
                        check = 0
                    }else{
                        val doc: Document = Jsoup
                            .connect(url)
                            .ignoreContentType(true)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        var str = doc.toString().substringAfter("{\"re")
                        while (str.contains("{\"re")){
                            val anime = Anime()
                            val s = str.substringBefore("{\"re")
                            anime.img = "https://www.animefreak.tv/meta/anime/"+s.substringAfter("id\":").substringBefore(",")+"/"+str.substringAfter("o_name\":\"").substringBefore("\"") +".jpg"
                            anime.link = "https://www.animefreak.tv/watch/" + str.substringAfter("o_name\":\"").substringBefore("\"")
                            anime.name = str.substringAfter("\"name\":\"").substringBefore("\"")
                            insert(anime)
                            str = str.substringAfter("{\"re")
                            i++
                        }
                        check = 0
                    }
                }
                else if (tag == "animefenix") {
                    if (genre=="genre"){
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements=doc.select("article.serie-card")
                        for (element in elements){
                            val anime = Anime()
                            anime.name = element.select("div.title").text()
                            anime.link = element.toString().substringAfter("href=\"").substringBefore("\"")
                            anime.img = element.toString().substringAfter("src=\"").substringBefore("\"")
                            insert(anime)
                            i++
                        }
                        check = 0
                    }else{
                        val doc: Document = Jsoup.connect(url)
                            .timeout(0)
                            .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                            .get()
                        val elements=doc.select("article.serie-card")
                        for (element in elements){
                            val anime = Anime()
                            anime.name = element.select("div.title").text()
                            anime.link = element.toString().substringAfter("href=\"").substringBefore("\"")
                            anime.img = element.toString().substringAfter("src=\"").substringBefore("\"")
                            insert(anime)
                            i++
                        }
                        check = 0
                    }
                }
                else if (tag == "jkanime") {
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                        .get()
                    val elements=doc.select("div.portada-box")
                    for (element in elements){
                        val anime = Anime()
                        anime.name = element.toString().substringAfter("title=\"").substringBefore("\"")
                        anime.link = element.toString().substringAfter("href=\"").substringBefore("\"")
                        anime.img = element.toString().substringAfter("src=\"").substringBefore("\"")
                        insert(anime)
                        i++
                    }
                    check = 0
                }
                else if (tag == "anyanime") {
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements=doc.select("div.anime-block")
                    for (element in elements){
                        val anime = Anime()
                        anime.name = element.select("div.anime-title-list").text()
                        anime.link = element.toString().substringAfter("href=\"").substringBefore("\"")
                        anime.img = element.toString().substringAfter("src=\"").substringBefore("\"")
                        insert(anime)
                        i++
                    }
                    check = 0
                }
            }catch (e:Exception){}
        }
        thread.start()
        val mainHanler = Handler(Looper.getMainLooper())
        mainHanler.post(object : Runnable{
            override fun run() {
                if (check==0){
                    progressBar.visibility = View.GONE
                }else mainHanler.postDelayed(this,1000)
            }
        })
    }
    fun insert (anime: Anime){
        listViewModel.insert( Top(anime.name, source,anime.img,anime.name+source, anime.link) )
    }
    companion object {

    }
    fun newInstance(url : String,source : String,genre:String) =
        FragmentDoubleListSearch(url, source, genre).apply {
            arguments = Bundle().apply {}
        }
}
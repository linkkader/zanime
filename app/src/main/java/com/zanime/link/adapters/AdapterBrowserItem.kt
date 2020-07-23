package com.zanime.link.adapters

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.type.Anime
import com.zanime.link.type.Item
import com.zanime.link.ui.MainActivity
import com.zanime.link.ui.fragment.FragmentDoubleList
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.util.*

class AdapterBrowserItem(var context : Context?, var list : List<Item>, var fragmentManager: FragmentManager, val sourceName:String) : RecyclerView.Adapter<AdapterBrowserItem.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(R.id.search_source_name)
        val recycler = itemView.findViewById<RecyclerView>(R.id.search_item_recycler)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_search,parent,false))
    }
    override fun getItemCount(): Int {
        return  list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = list[position].name.toUpperCase(Locale.ROOT)
        holder.text.setOnClickListener{
            if (!verifyAvailableNetwork(context as AppCompatActivity)){
                Toast.makeText(context, (context as AppCompatActivity).getString(R.string.verify_internet),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            fragmentManager.beginTransaction()
                .add(R.id.container_main, FragmentDoubleList(list[position].url,sourceName,"top"))
                .addToBackStack(null)
                .commit()
        }
        if (verifyAvailableNetwork(context as AppCompatActivity)){
            jsoupResponse (list[position].url.replace("linkkader","1") ,sourceName,holder.recycler,holder.progressBar)
        }
        jsoupResponse (list[position].url.replace("linkkader","1") ,sourceName,holder.recycler,holder.progressBar)
    }
    private fun jsoupResponse (url : String, tag : String,recycler : RecyclerView,progressBar: ProgressBar){
        var check = 0
        var animes = listOf<Anime>()
        val thread : Thread = Thread {
            try {

                if (tag=="voiranime"){
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
                        animes=animes.plus(anime)
                    }
                    check = 1
                }
                else if (tag == "gogoanime") {
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
                        animes=animes.plus(anime)
                    }
                    check = 1
                }
                else if (tag == "animefreak") {
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                        .get()
                    val elements = doc.select("div.bl-box")
                    for (element in elements) {
                        var str = element.toString()
                        val anime = Anime()
                        anime.name = element.select("a.bld-title").text()
                        anime.link = str.substringAfter("href=\"").substringBefore("\"")
                        anime.img = str.substringAfter("src=\"").substringBefore("\"").replace(" ","%20")
                        animes=animes.plus(anime)
                    }
                    check = 1
                }
                else if (tag == "vostfree") {
                    val doc = Jsoup .connect(url)
                        .timeout(0)
                        .maxBodySize(0)
                        .get()
                    val elements=doc.getElementById("dle-content").select("div.movie-poster")
                    for (element in elements) {
                        var str = element.select("a").toString()
                        val anime = Anime()
                        anime.img = "https://vostfree.com"+element.toString().substringAfter("src=\"").substringBefore("\"")
                        anime.name = str.substringAfter("alt=\"").substringBefore("\"")
                        anime.link=str.substringAfter("short-poster").substringAfter("href=\"").substringBefore("\"")
                        if(anime.name!="")animes = animes.plus(anime)
                    }
                    check = 1
                }
                else if (tag == "otakufr") {
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
                        animes=animes.plus(anime)
                        i2+=2
                    }
                    check = 1
                }
                else if (tag == "animedao") {
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                        .get()
                    val elements = doc.select("div.col-xs-6")
                    for (element in elements) {
                        val str = element.toString().substringAfter("href=\"")
                        val anime = Anime()
                        anime.link = str.substringAfter("href=\"").substringBefore("\"")
                        anime.img = anime.link.replace("anime","images").substringBeforeLast("/").substringBefore("-dubbed")+".jpg"
                        anime.link = "https://animedao.com"+anime.link
                        anime.img = "https://animedao.com"+anime.img
                        anime.name = element.text()
                        animes = animes.plus(anime)
                        check = 1
                    }
                }
                else if (tag == "ianime") {
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                        .get()
                    val elements = doc.select("tbody")
                    for (element in elements) {
                        if (element.select("tbody").size>=2) continue
                        var str = element.toString()
                        val anime = Anime()
                        anime.name = element.select("span").text()
                        anime.img = str.substringAfter("url('").substringBefore("'")
                        anime.link = "https://www.ianimes.org/"+str.substringAfter("href=\"").substringAfter("href=\"").substringBefore("\"")
                        if (anime.img.contains("jpg")) animes = animes.plus(anime)
                    }
                    check = 1
                }
                else if (tag == "animefreak") {
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
                        animes = animes.plus(anime)

                    }
                    check = 1
                }
                else if (tag == "animefenix") {
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
                        animes = animes.plus(anime)
                    }
                    check = 1
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
                        animes = animes.plus(anime)
                    }
                    check = 1
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
                        anime.link = element.toString().substringAfter("src=\"").substringAfter("href=\"").substringBefore("\"")
                        anime.img = element.toString().substringAfter("src=\"").substringBefore("\"")
                        animes = animes.plus(anime)
                    }
                    check = 1
                }
            }catch (e:Exception){}
        }
        thread.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable{
            override fun run() {
                if (check ==1 ){
                    progressBar.visibility = View.GONE
                    recycler.setHasFixedSize(true)
                    recycler.layoutManager =LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                    recycler.adapter = AdapterItemSearch(context,animes,sourceName)
                }else mainHandler.postDelayed(this,1000)
            }
        })
    }
    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}

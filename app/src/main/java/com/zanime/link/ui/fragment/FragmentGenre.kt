package com.zanime.link.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterGenres
import com.zanime.link.type.Genre
import com.zanime.link.type.Source
import org.jsoup.Jsoup


class FragmentGenre(val sourName: String = "") : Fragment() {
    var genres = listOf<Genre>()
    var check = 0
    var list = listOf<Source>()
    lateinit var recycler :RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.double_list)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        jsoupResponse(sourName)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }
    fun jsoupResponse (source : String){
        val thread : Thread = Thread {
            if (source=="voiranime"){
                val doc = Jsoup.connect("https://voiranime.to/anime-genre/fantasy/")
                    .timeout(0)
                    .maxBodySize(0)
                    .get()
                val elements = doc.select("ul.list-unstyled").select("li")
                for (element in elements){
                    val genre = Genre()
                    genre.url =element.toString().substringAfter("href=\"").substringBefore("\"")+"page/linkkader/"
                    genre.name = element.text()
                    genres = genres.plus(genre)
                    check = 1
                }
            }
            else if (source=="gogoanime"){
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/School?page=linkkader","School"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/action?page=linkkader","Action"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/supernatural?page=linkkader","supernatural"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/comedy?page=linkkader","Comedy"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/martial-arts?page=linkkader","Martials Arts"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/shounen?page=linkkader","Shounen"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/super-power?page=linkkader","Super Power"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/adventure?page=linkkader","Adventure"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/drama?page=linkkader","Drama"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/harem?page=linkkader","Harem"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/seinen?page=linkkader","Seinen"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/fantasy?page=linkkader","Fantasy"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/sci-fi?page=linkkader","Sci-Fi"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/romance?page=linkkader","Romance"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/shoujo?page=linkkader","Shoujo"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/slice-of-life?page=linkkader","Slice of Life"))
                genres=genres.plus(Genre("http://www19.gogoanime.io/genre/music?page=linkkader","Music"))
                check = 1
            }
            else if (source == "otakufr"){
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/Jeu/name-az/linkkader/","Jeu"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/Tranche%20de%20vie/name-az/linkkader/","Tranche de vie"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/crime/name-az/linkkader/ ","Crime"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/samurai/name-az/linkkader/","Samourai"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/thriller/name-az/linkkader/","Thriller"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/dramee/name-az/linkkader/","Drame"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/comedie/name-az/linkkader/","Comedie"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/ecchi/name-az/linkkader/","Ecchi"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/romance/name-az/linkkader/","Romance"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/Myst%C3%A9rieux/name-az/linkkader/","Mysterieux"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/harem/name-az/linkkader/ ","Harem"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/mecha/name-az/linkkader/","Mecha"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/sport/name-az/linkkader/","Sport"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/aventure/name-az/linkkader/","Aventure"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/vampire/name-az/linkkader/ ","Vampire"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/suspense/name-az/linkkader/","Suspense"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/surnaturel/name-az/linkkader/","Surnaturel"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/Shoujo%20Ai/name-az/linkkader/","Shoujo Ai"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/parodie/name-az/linkkader/","Parodie"))
                genres = genres.plus(Genre("https://www.otakufr.com/anime-list/category/espace/name-az/linkkader/ ","Espace"))
                check=1
            }
            else if (source == "anyanime"){
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d9%82%d9%88%d9%89-%d8%ae%d8%a7%d8%b1%d9%82%d9%87/page/linkkader/","قوى-خارقه"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d8%b4%d9%88%d9%86%d9%8a%d9%86/page/linkkader/","شونين"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d8%a7%d9%83%d8%b4%d9%86/page/linkkader/ ","اكشن"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d9%85%d8%af%d8%b1%d8%b3%d9%8a/page/linkkader/","مدرسي"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d9%83%d9%88%d9%85%d9%8a%d8%af%d9%8a/page/linkkader/","كوميدي"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d8%ae%d9%8a%d8%a7%d9%84-%d8%b9%d9%84%d9%85%d9%8a/page/linkkader/","خيال-علمي"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d9%85%d8%ba%d8%a7%d9%85%d8%b1%d8%a7%d8%aa/page/linkkader/","مغامرات"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d8%af%d8%b1%d8%a7%d9%85%d8%a7/page/linkkader/","دراما"))
                genres = genres.plus(Genre("https://ww8.anyanime.com/series_online_category/%d9%81%d8%a7%d9%86%d8%aa%d8%a7%d8%b2%d9%8a%d8%a7/page/linkkader/","فانتازيا"))
                check=1
            }
            else if (source == "vostfree"){
                val doc = Jsoup .connect("https://vostfree.com/genre/Action")
                    .timeout(0)
                    .maxBodySize(0)
                    .get()
                val elements = doc.select("ul.cat-list").select("li")
                for (element in elements){
                    val genre = Genre()
                    genre.url="https://vostfree.com"+element.toString().substringAfter("href=\"").substringBefore("\"")+"page/linkkader/"
                    genre.name = element.text()
                    genres=genres.plus(genre)
                }
                check = 1
            }
            else if (source == "animedao"){
                val doc = Jsoup .connect("https://animedao.com/animelist/genre/list")
                    .timeout(0)
                    .maxBodySize(0)
                    .get()
                val elements = doc.select("div.animelist_alfabet_nav").select("a")
                for (element in elements){
                    val genre = Genre()
                    genre.url="https://animedao.com"+element.toString().substringAfter("href=\"").substringBefore("\"")
                    genre.name = element.text()
                    genres=genres.plus(genre)
                }
                check = 1
            }
            else if (source == "ianime"){
                val doc = Jsoup .connect("https://www.ianimes.org/index.php")
                    .timeout(0)
                    .maxBodySize(0)
                    .get()
                val elements = doc.select("tbody")[0].select("a")
                for (element in elements){
                    val genre = Genre()
                    genre.url="https://www.ianimes.org/"+element.toString().substringAfter("href=\"").substringBefore("\"").replace("&amp;","&")
                    genre.name = element.text()
                    genres=genres.plus(genre)
                }
                check = 1
            }
            else if (source == "animefreak"){
                val doc = Jsoup .connect("https://www.animefreak.tv/home/genres")
                    .timeout(0)
                    .maxBodySize(0)
                    .get()
                val elements = doc.select("ul.arrow-list")[1].select("a")
                for (element in elements){
                    val genre = Genre()
                    genre.url=element.toString().substringAfter("href=\"").substringBefore("\"") + "/page/linkkader"
                    genre.name = element.text()
                    genres=genres.plus(genre)
                }
                check = 1
            }
            else if (source == "animefenix"){
                val doc = Jsoup .connect("https://www.animefenix.com/animes")
                    .timeout(0)
                    .maxBodySize(0)
                    .get()
                val elements = doc.getElementById("genre_select").select("option")
                for (element in elements){
                    val genre = Genre()
                    genre.url="https://www.animefenix.com/animes?genero%5B%5D=linkkader&order=default".replace("linkkader",element.toString().substringAfter("value=\"").substringBefore("\""))+"&page=linkkader"
                    genre.name = element.text()
                    genres=genres.plus(genre)
                }
                check = 1
            }
            else if (source == "jkanime"){
                val doc = Jsoup .connect("https://jkanime.net/letra/0-9/")
                    .timeout(0)
                    .maxBodySize(0)
                    .get()
                val elements = doc.select("div.genre-list").select("li")
                for (element in elements){
                    val genre = Genre()
                    genre.url="https://jkanime.net"+element.toString().substringAfter("href=\"").substringBefore("\"")+"linkkader/"
                    genre.name = element.text()
                    genres=genres.plus(genre)
                }
                check = 1
            }
        }
        thread.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable{
            override fun run() {
                if (check == 1){
                    recycler.adapter = AdapterGenres(context,genres,activity!!.supportFragmentManager,sourName)
                }
                else mainHandler.postDelayed(this,1000)
            }
        })
    }
}
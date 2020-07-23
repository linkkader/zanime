package com.zanime.link.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.sackcentury.shinebuttonlib.ShineButton
import com.squareup.picasso.Picasso
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.adapters.AdapterEpisode
import com.zanime.link.adapters.AdapterGenre
import com.zanime.link.adapters.AdapterSeason
import com.zanime.link.database.library.Library
import com.zanime.link.database.library.LibraryViewModel
import com.zanime.link.type.*
import com.zanime.link.ui.MainActivity
import com.zanime.link.ui.VodActivity
import kotlinx.android.synthetic.main.info.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.sql.Date
import java.util.*

class FragmentAnimeInfo(val url:String,val source : String) : Fragment() {
    var check = 0
    var list = listOf<Library>()
    lateinit var listViewModel : LibraryViewModel
    var animeInfo = AnimeInfo()
    companion object{
        var str = ""
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animeInfo.url = url
        val like : ShineButton = view.findViewById(R.id.btn)
        val img : RoundedImageView = view.findViewById(R.id.poster)
        val native : TextView = view.findViewById(R.id.nativ)
        val nativeText : TextView = view.findViewById(R.id.nativtext)
        val status : TextView = view.findViewById(R.id.status)
        val statuText : TextView = view.findViewById(R.id.statustext)
        val start : TextView = view.findViewById(R.id.start)
        val startText : TextView = view.findViewById(R.id.starttext)
        val end : TextView = view.findViewById(R.id.end)
        val endText : TextView = view.findViewById(R.id.endtext)
        val studios : TextView = view.findViewById(R.id.studios)
        val studiosText : TextView = view.findViewById(R.id.studiotext)
        val sourceText : TextView = view.findViewById(R.id.sourcetext)
        val description : TextView = view.findViewById(R.id.description)
        val title : TextView = view.findViewById(R.id.title)
        val backgoundimg : ImageView = view.findViewById(R.id.backgroundimg)
        val recyclerCategory : RecyclerView = view.findViewById(R.id.recyclercategory)
        val recyclerSeason : RecyclerView = view.findViewById(R.id.recycler_season)
        listViewModel = ViewModelProvider(this@FragmentAnimeInfo).get(LibraryViewModel(activity!!.application)::class.java)
        jsoupResponse(url,source)
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (check==1){
                    Picasso.get().load(animeInfo.img).error(R.drawable.vstream).into(img)
                    Picasso.get().load(animeInfo.img).error(R.drawable.vstream).into(backgoundimg)
                    nativeText.text = animeInfo.native
                    if(animeInfo.name=="") animeInfo.name = com.zanime.link.ui.AnimeInfo.name
                    title.text = animeInfo.name
                    endText.text = animeInfo.endDate
                    startText.text = animeInfo.startDate
                    description.text = animeInfo.description
                    studiosText.text = animeInfo.studio
                    statustext.text = animeInfo.status
                    sourceText.text = animeInfo.source
                    recyclerCategory.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerCategory.setHasFixedSize(true)
                    str = animeInfo.source+animeInfo.name
                    str = str.replace("/","")
                    recyclerCategory.adapter = AdapterGenre(activity,animeInfo.genres)
                    recyclerSeason.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerSeason.setHasFixedSize(true)
                    like.visibility = View.VISIBLE
                    recyclerSeason.adapter = AdapterSeason(activity,animeInfo.seasons,animeInfo.img)
                    FragmentEpisode.thisAdapter = AdapterEpisode(context!!.applicationContext,animeInfo.episodes,animeInfo)
                    FragmentEpisode.recyclerView.adapter = FragmentEpisode.thisAdapter
                    FragmentEpisode.resume.setOnClickListener{
                        if (UnityAds.isReady("inters")&& MainActivity.bool){
                            UnityAds.show(context as Activity?,"inters")
                        }
                        val sharedPreferences = context!!.getSharedPreferences(str, Context.MODE_PRIVATE)
                        val position = sharedPreferences.getInt("resumePosition",0)
                        if (position<animeInfo.episodes.size){
                            VodActivity.sourceName = animeInfo.source
                            VodActivity.position = position
                            VodActivity.episodes = animeInfo.episodes
                            context!!.startActivity(Intent(context,VodActivity::class.java))
                        }
                    }
                    listViewModel.allWords.observe(viewLifecycleOwner , Observer { lists ->
                        for (list in lists) {
                            if (list.source==animeInfo.source&&list.name==animeInfo.name){
                                like.isChecked = true
                            }
                        }
                    })
                    like.setOnClickListener{
                        if (like.isChecked){
                            Toast.makeText(activity,animeInfo.name+" "+ activity!!.getString(R.string.added_to_library),Toast.LENGTH_LONG).show()
                            listViewModel.insert(Library(animeInfo.name,animeInfo.img
                                ,animeInfo.source,animeInfo.source+animeInfo.name
                                , Date(java.util.Date().time).toString(),url))
                        }else{
                            listViewModel.delete(animeInfo.source,animeInfo.name)
                        }
                    }

                }else{
                    mainHandler.postDelayed(this,1000)
                }
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_anime_info, container, false)
    }
    fun jsoupResponse (url : String,source : String){
        val thread : Thread = Thread {
            try {

                if (source=="voiranime"){
                    val doc = Jsoup.connect(url).timeout(0).maxBodySize(0).get()
                    var elements =doc.select("div.post-content").select("div.post-content_item")
                    for (element in  elements) {
                        val str=element.toString()
                        if (str.contains("Native"))  animeInfo.native=element.select("div.summary-content").text()
                        else if (str.contains("Romaji"))  animeInfo.name=element.select("div.summary-content").text()
                        else if (str.contains("English"))  animeInfo.english=element.select("div.summary-content").text()
                        else if (str.contains("French"))  animeInfo.french=element.select("div.summary-content").text()
                        else if (str.contains("Rating"))  animeInfo.rating=element.select("div.summary-content").text()
                        else if (str.contains("Type"))  animeInfo.type=element.select("div.summary-content").text()
                        else if (str.contains("Status"))  animeInfo.status=element.select("div.summary-content").text()
                        else if (str.contains("Studios"))  animeInfo.studio=element.select("div.summary-content").text()
                        else if (str.contains("Episodes"))  animeInfo.nbrEpisodes=element.select("div.summary-content").text()
                        else if (str.contains("Start date"))  animeInfo.startDate=element.select("div.summary-content").text()
                        else if (str.contains("End date"))  animeInfo.endDate=element.select("div.summary-content").text()
                        else if (str.contains("Genre"))  {
                            animeInfo.genre=element.select("div.summary-content").text()
                            val elts = element.select("a")
                            for (elt in elts){
                                val genre = Genre()
                                genre.name = elt.text()
                                genre.url = elt.toString().substringAfter("href=\"").substringBefore("\"")
                                animeInfo.genres = animeInfo.genres.plus(genre)
                            }
                        }
                    }
                    animeInfo.description=doc.select("div.description-summary").select("p").text()
                    if(doc.select("div.summary_image").toString().contains("png")){
                        animeInfo.img=doc.select("div.summary_image").toString().substringAfter("srcset=\"").substringBefore(",").substringBeforeLast("-")+".png"
                    }else animeInfo.img=doc.select("div.summary_image").toString().substringAfter("srcset=\"").substringBefore(",").substringBeforeLast("-")+".jpg"
                    elements=doc.select("div.page-content-listing").select("li.wp-manga-chapter").select("a")
                    for (element in elements){
                        val episode= Episode()
                        episode.name=element.text()
                        episode.url=element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.episodes=animeInfo.episodes.plus(episode)
                    }
                    animeInfo.source = "voiranime"
                    check=1
                }
                else if (source=="ianime"){
                    var doc = Jsoup.connect(url).timeout(0).maxBodySize(0).get()
                    if (doc.select("li.cat_post_item-1").toString().contains("Synopsis")){
                        val episode = Episode()
                        episode.name = com.zanime.link.ui.AnimeInfo.name
                        episode.url=url
                        animeInfo.episodes = animeInfo.episodes.plus(episode)
                        val url = "https://www.ianimes.org/"+doc.select("li.cat_post_item-1").toString().substringAfter("href=\"").substringBefore("\"")
                        doc = Jsoup.connect(url).timeout(0).maxBodySize(0).get()
                    }
                    var elements =doc.select("div.post-wrapper").select("fieldset")[0].select("font")
                    var i = 0
                    while ((i+1)<elements.size){
                        if (elements[i].toString().contains("Titre al")){
                            animeInfo.name = elements[i+1].text()
                        }
                        else if (elements[i].toString().contains("Titre or")){
                            animeInfo.native = elements[i+1].text()
                        }
                        else if (elements[i].toString().contains("Pays")){
                            animeInfo.country = elements[i+1].text()
                        }
                        else if (elements[i].toString().contains("Format")){
                            animeInfo.country = elements[i+1].text()
                        }
                        else if (elements[i].toString().contains("Format")){
                            animeInfo.country = elements[i+1].text()
                        }
                        else if (elements[i].toString().contains("Date de Diff")){
                            animeInfo.startDate = elements[i+1].text()
                        }
                        else if (elements[i].toString().contains("Durée")){
                            animeInfo.duration = elements[i+1].text()
                        }
                        else if (elements[i].toString().contains("Studio")){
                            animeInfo.studio = elements[i+1].text()
                        }
                        i++
                    }
                    animeInfo.description = doc.select("div.post-wrapper").select("fieldset")[1].select("font").text()
                    animeInfo.img = doc.select("tr").toString().substringAfter("src=\"").substringBefore("\"")
                    elements = doc.select("a.genre")
                    for (element in elements){
                        val genre = Genre()
                        genre.name = element.text()
                        genre.url ="https://www.ianimes.org/"+ element.toString().substringAfter("href=\"").substringBefore("\"")
                        genre.url = genre.url.replace("&amp;","&")
                        animeInfo.genres = animeInfo.genres.plus(genre)
                    }
                    elements = doc.select("li.cat_post_item-1").select("a")
                    for (element in elements){
                        val episode = Episode()
                        episode.name = element.toString().substringAfter("title=\"").substringBefore("\"")
                        episode.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        if(episode.url.contains("https"))animeInfo.episodes = animeInfo.episodes.plus(episode)
                    }
                    if (animeInfo.episodes.size==0&&animeInfo.genres[0].name.toLowerCase().contains("vision")){
                        val episode = Episode()
                        episode.name = com.zanime.link.ui.AnimeInfo.name
                        episode.url=animeInfo.genres [0].url
                        animeInfo.episodes = animeInfo.episodes.plus(episode)
                    }
                    animeInfo.source = "ianime"
                    animeInfo.episodes = animeInfo.episodes.asReversed()
                    check=1
                }
                else if (source=="animedao"){
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                        .get()
                    var elements = doc.select("div.col-xs-3")
                    animeInfo.img ="https://animedao.com"+ elements.toString().substringAfter("data-src=\"").substringBefore("\"")
                    elements = doc.select("div.col-xs-9")
                    animeInfo.name = elements.select("h2").text()
                    str = elements.toString()
                    animeInfo.native = str.substringAfter("Alternative:</b>").substringBefore("<")
                    animeInfo.rating = str.substringAfter("Rating:</b>").substringBefore("<")
                    animeInfo.startDate = str.substringAfter("Year:</b>").substringBefore("<")
                    animeInfo.status = str.substringAfter("Status:</b>").substringBefore("<")

                    animeInfo.description = doc.select("div.collapse").text()
                    elements = doc.select("div.col-xs-9").select("a")
                    for (element in elements){
                        val genre = Genre()
                        genre.name = element.text()
                        genre.url = "https://animedao.com/"+element.toString().substringAfter("href=\"")
                        animeInfo.genres = animeInfo.genres.plus(genre)
                    }
                    elements = doc.select("a.episode_well_link")
                    for (element in elements){
                        val episode = Episode()
                        episode.name = element.text().substringAfter(" ").substringAfter(" ").substringAfter(" ")
                        episode.url = "https://animedao.com"+element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.episodes = animeInfo.episodes.plus(episode)
                    }
                    elements = doc.select("div.list-group").select("a")
                    for (element in elements){
                        val episode = Episode()
                        episode.name = element.text()
                        episode.url = "https://animedao.com"+element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.episodes = animeInfo.episodes.plus(episode)
                    }
                    animeInfo.source = "animedao"
                    animeInfo.episodes = animeInfo.episodes.asReversed()
                    check=1
                }
                else if (source=="gogoanime"){
                    val doc: Document = Jsoup.connect("https://www19.gogoanime.io/$url")
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                        .get()
                    val s=Season()
                    s.name=doc.select("div.anime_video_body").toString().substringBeforeLast("</a>").substringAfterLast("-")
                    s.name="Episodes 1-${s.name}"
                    s.link=url
                    var size= 0
                    try {
                        size = s.name.substringAfterLast("-").toInt()
                    }catch (e:Exception){}
                    var i = 1
                    while (true){
                        if (size == 0) break
                        val episode= Episode()
                        episode.name = s.link.substringAfterLast("/").replace("-"," ")+" "+i.toString()
                        episode.url = "https://www19.gogoanime.io"+"${s.link}-episode-$i".replace("/category","")
                        animeInfo.episodes=animeInfo.episodes.plus(episode)
                        i++
                        if(i>size)break
                    }
                    //if(!doc.select("div.anime_video_body").toString().contains("ep_end=\"0\""))animeInfo.seasons=animeInfo.seasons.plus(s)
                    var elements=doc.select("div.anime_info_body_bg")
                    val str = elements.toString()
                    animeInfo.img = str.substringAfter("src=\"").substringBefore("\"")
                    animeInfo.name = str.substringAfter("<h1>").substringBefore("</h1>")
                    elements=elements.select("p.type")
                    animeInfo.type=elements[0].toString().substringBefore("</a>").substringAfterLast(">")
                    animeInfo.description= elements[1].toString().substringAfter("</span>").substringBefore("</p>")
                    for (element in elements[2].select("a")){
                        val genre = Genre()
                        genre.name = element.text().replace(",","")
                        genre.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.genres = animeInfo.genres.plus(genre)

                        animeInfo.genre+=element.text()
                    }
                    animeInfo.startDate= elements[3].toString().substringAfter("</span>").substringBefore("</p>")
                    animeInfo.status= elements[4].toString().substringAfter("</span>").substringBefore("</p>")
                    animeInfo.native= elements[5].toString().substringAfter("</span>").substringBefore("</p>")
                    animeInfo.episodes = animeInfo.episodes.asReversed()
                    animeInfo.source = "gogoanime"
                    check=1
                }
                else if (source=="animefreak"){
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10) // Size in Bytes - 10 MB
                        .get()
                    var elements = doc.select("div.animeDetail-top")
                    var str = elements.toString()
                    animeInfo.img = str.substringAfter("src=\"").substringBefore("\"")
                    animeInfo.name = str.substringAfter("title=\"").substringBefore("\"")
                    animeInfo.description = elements.select("p.anime-details").text()
                    elements = doc.select("ul.check-list")
                    if (elements.size==2) elements = elements[1].select("li")
                    else if (elements.size==1) elements = elements.select("li")
                    for (element in elements){
                        val episode = Episode()
                        episode.name = element.text()
                        episode.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.episodes = animeInfo.episodes.plus(episode)
                    }
                    animeInfo.source = "animefreak"
                    check=1
                }
                else if (source=="vostfree"){
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    var elements=doc.select("div.slide-block").select("div.slide-middle")
                    animeInfo.name = elements.select("h1").text()
                    animeInfo.description = elements.select("div.slide-desc").text()
                    animeInfo.acteur = animeInfo.description.substringAfter("Acteur:").substringBefore("...")
                    animeInfo.studio = animeInfo.description.substringAfter("Réalisateur:").substringBefore("...")
                    animeInfo.description=animeInfo.description.substringBefore("...")
                    val elts = elements.select("ul.slide-top")[1].select("a")
                    for (elt in elts){
                        val genre = Genre()
                        genre.name = elt.text()
                        genre.url = elt.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.genres = animeInfo.genres.plus(genre)
                    }
                    animeInfo.genre = elements.select("ul.slide-top")[1].text().substringAfter("Genre:")
                    animeInfo.startDate = elements.select("ul.slide-top")[0].text()
                    animeInfo.img="https://vostfree.com"+doc.select("div.slide-poster").toString().substringAfter("src=\"").substringBefore("\"")
                    elements = doc.select("div.new_player_bottom").select("div.button_box")
                    var i = 1
                    for (element in elements) {
                        val ep = Episode()
                        ep.name = "Episode $i"
                        i++
                        val elts = element.select("div")
                        var i2 = 0
                        for (e in elts) {
                            if (i2==0){
                                i2 =1
                                continue
                            }
                            val server = Server()
                            server.name = e.text()
                            server.id = e.toString().substringAfter("\"").substringBefore("\"")
                            ep.servers = ep.servers.plus(server)
                        }
                        animeInfo.episodes = animeInfo.episodes.plus(ep)
                    }
                    elements = doc.select("div.tab-content")[0].select("div")
                    i=0
                    var servers = listOf<Server>()
                    for (element in elements) {
                        i++
                        if (i<3)continue
                        val server = Server()
                        server.id=element.toString().substringAfter("\"").substringBefore("\"")
                        server.url=element.text()
                        servers = servers.plus(server)
                    }
                    var episodes2 = listOf<Episode>()
                    for (episode in animeInfo.episodes){
                        for (server in episode.servers){
                            var i = 0
                            while (i<servers.size){
                                if (servers[i].id.substringAfter("_")==server.id)
                                {
                                    if (server.name.toLowerCase(Locale.ROOT).contains("sibnet")) server.url ="https://video.sibnet.ru/shell.php?videoid="+ servers[i].url
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("gtv")) server.url ="https://iframedream.com/embed/"+ servers[i].url+".html"
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("mytv")) server.url ="https://www.myvi.tv/embed/"+ servers[i].url
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("stream")) server.url ="https://www.myvi.tv/embed/"+ servers[i].url
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("kaztube")) server.url ="https://kaztube.kz/video/embed/"+ servers[i].url
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("uqload")) server.url ="https://uqload.com/embed-"+ servers[i].url+".html"
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("vfree")) server.url ="https://www.fembed.com/v/"+ servers[i].url+".html"
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("ok")) server.url ="https://www.ok.ru/videoembed/"+ servers[i].url
                                    else if (server.name.toLowerCase(Locale.ROOT).contains("mail")) server.url ="https://my.mail.ru/video/embed/"+ servers[i].url
                                    else server.url= servers[i].url
                                }
                                i++
                            }
                        }
                        episodes2 = episodes2.plus(episode)
                    }
                    animeInfo.episodes = episodes2
                    animeInfo.episodes = animeInfo.episodes.asReversed()
                    animeInfo.source = "vostfree"
                    check=1
                }
                else if (source=="otakufr"){
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    var elements=doc.select("div.anm_ifo")
                    animeInfo.img = elements.toString().substringAfter("src=\"").substringBefore("\"")
                    animeInfo.name = elements.toString().substringAfter("alt=\"").substringBefore("\"")
                    elements=elements.select("p")
                    for(element in elements){
                        if (element.toString().contains("Synopsis")) animeInfo.description=element.text().substringAfter(":")
                        else if (element.toString().contains("Autre Nom")) animeInfo.native=element.text().substringAfter(":")
                        else if (element.toString().contains("Auteur")) animeInfo.studio=element.text().substringAfter(":")
                        else if (element.toString().contains("Date Ajoutée")) animeInfo.startDate=element.text().substringAfter(":")
                        else if (element.toString().contains("Durée")) animeInfo.duration=element.text().substringAfter(":")
                        else if (element.toString().contains("Statut")) animeInfo.status=element.text().substringAfter(":")
                    }
                    elements = doc.select("ul.lst").select("li.lng_")
                    for (element in elements) {
                        val ep = Episode()
                        ep.name=element.text().substringBeforeLast("-")
                        ep.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.episodes = animeInfo.episodes.plus(ep)
                    }
                    animeInfo.source = "otakufr"
                    check=1
                }
                else if (source=="animefenix"){
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    var elements=doc.select("div.column")
                    animeInfo.img = elements.select("img").toString().substringAfter("src=\"").substringBefore("\"")
                    animeInfo.name = elements.select("h1.title").text()
                    animeInfo.description = elements.select("p.has-text-light").text()
                    val elements2 = elements.select("p.genres").select("a")
                    for (element in elements2){
                        val genre = Genre()
                        genre.name = element.text()
                        genre.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.genres = animeInfo.genres.plus(genre)
                    }
                    elements = elements.select("ul.has-text-light").select("li")
                    for (element in elements){
                        if (str.contains("Tipo:")) animeInfo.type = element.text().substringAfter(":")
                        else if (str.contains("Estado:")) animeInfo.status  = element.text().substringAfter(":")
                        else if (str.contains("Episodios:")) animeInfo.nbrEpisodes = element.text().substringAfter(":")
                    }
                    elements = doc.select("ul.anime-page__episode-list").select("li")
                    for (element in elements) {
                        val ep = Episode()
                        ep.name=element.text()
                        ep.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.episodes = animeInfo.episodes.plus(ep)
                    }
                    animeInfo.source = "animefenix"
                    animeInfo.episodes = animeInfo.episodes.asReversed()
                    check=1
                }
                else if (source=="anyanime"){
                    var doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    var elements=doc.select("div.eng-warp")
                    elements = elements.select("div.engs-content")
                    elements = elements.select("div.cover")
                    val a = elements.toString().substringAfter("href=\"").substringBefore("\"")
                    doc = Jsoup.connect(a)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    elements = doc.select("div.anime-block-show")
                    animeInfo.img = elements.toString().substringAfter("src=\"").substringBefore("\"")
                    animeInfo.name = elements.select("div.anime-title-show").text()
                    animeInfo.description = elements.select("div.anime-content-show").text()
                    animeInfo.startDate = elements.select("div.anime-date-show").text()
                    animeInfo.status = elements.select("div.anime-how-show").text()
                    animeInfo.nbrEpisodes = elements.select("div.anime-eps-show").text()
                    elements = elements.select("div.anime-genre-show").select("a")
                    for (element in elements){
                        val genre = Genre()
                        genre.name = element.text()
                        genre.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.genres = animeInfo.genres.plus(genre)
                    }
                    elements = doc.select("ul.list").select("a")
                    for (element in elements){
                        val episode = Episode()
                        episode.name = element.text()
                        episode.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        animeInfo.episodes = animeInfo.episodes.plus(episode)
                    }
                    animeInfo.source = "anyanime"
                    animeInfo.episodes = animeInfo.episodes.asReversed()
                    check = 1
                }
                else if (source=="jkanime"){
                    val doc: Document = Jsoup.connect(url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    var elements=doc.select("div.serie-info")
                    animeInfo.img = elements.select("img").toString().substringAfter("src=\"").substringBefore("\"")
                    animeInfo.description = elements.select("p.pc").text().substringAfter(":")
                    animeInfo.name = elements.select("h2").text()
                    elements = doc.select("div.info-content")
                    elements = elements.select("div.info-field")
                    for (element in elements){
                        val str = element.toString()
                        if (str.contains("Tipo:")) animeInfo.type = element.text().substringAfter(":")
                        else if (str.contains("Idiomas:")) animeInfo.country = element.text().substringAfter(":")
                        else if (str.contains("Idiomas:")) animeInfo.country = element.text().substringAfter(":")
                        else if (str.contains("Episodios:")) animeInfo.nbrEpisodes = element.text().substringAfter(":")
                        else if (str.contains("Duracion:")) animeInfo.duration = element.text().substringAfter(":")
                        else if (str.contains("Emitido:")) animeInfo.duration = element.text().substringAfter(":")
                        else if (str.contains("Estado:")) animeInfo.duration = element.text().substringAfter(":")
                        else if (str.contains("Genero:")) {
                            val elts = element.select("a")
                            for (e in elts){
                                val genre = Genre()
                                genre.name = e.text()
                                genre.url = e.toString().substringAfter("href=\"").substringBefore("\"")+"linkkader/"
                            }
                        }
                    }
                    elements = doc.select("a.numbers")
                    var i = 0
                    var a = 0
                    if (elements.size>0){
                        i = elements[0].text().substringBefore(" -").toInt()
                        a = elements[elements.size - 1].text().substringAfter("- ").toInt()
                    }
                    while (a>=i){
                        val ep = Episode()
                        ep.name="Capitulo $a"
                        ep.url = "$url/$a"
                        animeInfo.episodes = animeInfo.episodes.plus(ep)
                        a--
                    }
                    animeInfo.source = "jkanime"
                    check=1
                }
            }catch (e:Exception){}
        }
        thread.start()
    }
}
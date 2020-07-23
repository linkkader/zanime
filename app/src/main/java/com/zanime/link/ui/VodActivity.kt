package com.zanime.link.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.gurudev.fullscreenvideowebview.FullScreenVideoWebView
import com.unity3d.ads.UnityAds
import com.zanime.link.type.Server
import com.zanime.link.R
import com.zanime.link.adapters.AdapterEpisodeVod
import com.zanime.link.adapters.AdapterServerVod
import com.zanime.link.type.Episode
import com.zanime.link.ui.fragment.FragmentAnimeInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VodActivity : AppCompatActivity() {
    companion object {
        fun saveState(){
            a.putLong(position.toString(), player.currentPosition)
            a.apply()
        }
        lateinit var a :SharedPreferences.Editor
        var sourceName = ""
        lateinit var episodeRecycler : RecyclerView
        lateinit var serverRecycler : RecyclerView
        lateinit var loading1: ProgressBar
        lateinit var playerView: PlayerView
        private var playWhenReady = true
        private var playbackPosition: Long = 0
        lateinit var player: SimpleExoPlayer
        var position =0
        var checkFinish=false
        lateinit var textTitle : TextView
        lateinit var fa:VodActivity
        var defaultBandwidthMeter = DefaultBandwidthMeter()
        lateinit var  dataSourceFactory: DataSource.Factory
        lateinit var mediaSource: MediaSource
        lateinit var serverButton:AppCompatImageView
        lateinit var episodeButton:AppCompatImageView
        var episodes = listOf<Episode>()
        lateinit var fullScreenVideoWebView: FullScreenVideoWebView
    }
    var justserver = Server()
    var boolVod = true
    var vod = ""
    lateinit var playerTo: AppCompatImageView
    lateinit var exit: AppCompatImageView
    lateinit var playerback: AppCompatImageView
    lateinit var playerNext: AppCompatImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_vod)
        if (supportActionBar!=null){
            supportActionBar!!.hide()
        }
        a = getSharedPreferences(FragmentAnimeInfo.str, Context.MODE_PRIVATE).edit()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        episodeRecycler=findViewById(R.id.episodes_list)
        episodeRecycler.setHasFixedSize(true)
        episodeRecycler.layoutManager= LinearLayoutManager(this)
        episodeRecycler.adapter = AdapterEpisodeVod(this, episodes, sourceName)
        serverRecycler = findViewById(R.id.server_list)
        serverRecycler.layoutManager = LinearLayoutManager(this)
        episodeButton = findViewById(R.id.episode)
        serverButton = findViewById(R.id.server)
        fullScreenVideoWebView = findViewById(R.id.webView)
        episodeButton.setOnClickListener{
            serverRecycler.visibility = View.GONE
            if (episodeRecycler.visibility == View.VISIBLE)
                episodeRecycler.visibility = View.GONE
            else episodeRecycler.visibility = View.VISIBLE
        }
        serverButton.setOnClickListener{
            episodeRecycler.visibility = View.GONE
            if (serverRecycler.visibility == View.VISIBLE)
                serverRecycler.visibility = View.GONE
            else serverRecycler.visibility = View.VISIBLE
        }
        playerView =findViewById(R.id.video_player)
        loading1 =findViewById(R.id.loading)
        fa=this
        playerback=findViewById(R.id.exo_playerUnder)
        playerback.setOnClickListener{
            player.seekTo(player.currentPosition-10000)
        }
        playerTo=findViewById(R.id.exo_playerTo)
        playerTo.setOnClickListener{
            player.seekTo(player.currentPosition+10000)
        }
        textTitle=findViewById(R.id.title)
        textTitle.text = episodes[position].name
        playerback=findViewById(R.id.exo_back)
        playerNext=findViewById(R.id.exo_next1)
        playerNext.setOnClickListener{
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(this,"inters")
            }
            position--
            if (position==-1){
                position=0
                return@setOnClickListener
            }
            saveState()
            val sharedPreference = getSharedPreferences(FragmentAnimeInfo.str,Context.MODE_PRIVATE)
            val edito = sharedPreference.edit()
            var string = sharedPreference.getString("episode","")
            string = "$string*$position*"
            edito.putString("episode",string)
            edito.putString("resume",episodes[position].name)
            edito.putInt("resumePosition", VodActivity.position)
            edito.apply()
            val sharedPreferences = getSharedPreferences(FragmentAnimeInfo.str,Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("resume",episodes[position].name)
            editor.putInt("resumePosition", position)
            editor.apply()
            playerView.visibility=View.GONE
            loading1.visibility=View.VISIBLE
            textTitle.text= episodes[position].name
            player.stop()
            getUrl(sourceName,episodes[position])
        }
        playerback.setOnClickListener{
            position++
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(this,"inters")
            }
            if (position==episodes.size){
                position--
                return@setOnClickListener
            }
            saveState()
            val sharedPreference = getSharedPreferences(FragmentAnimeInfo.str,Context.MODE_PRIVATE)
            val edito = sharedPreference.edit()
            var string = sharedPreference.getString("episode","")
            string = "$string*$position*"
            edito.putString("episode",string)
            edito.putString("resume",episodes[position].name)
            edito.putInt("resumePosition", VodActivity.position)
            edito.apply()

            val sharedPreferences = getSharedPreferences(FragmentAnimeInfo.str,Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("resume",episodes[position].name)
            editor.putInt("resumePosition", position)
            editor.apply()
            playerView.visibility=View.GONE
            loading1.visibility=View.VISIBLE
            textTitle.text= episodes[position].name
            player.stop()
            getUrl(sourceName,episodes[position])
        }

        playerNext.visibility =View.VISIBLE
        playerback.visibility= View.VISIBLE

        exit=findViewById(R.id.exit_activity)
        exit.setOnClickListener{
            player.stop()
            finish()
        }
        getUrl(sourceName, episodes[position])
        val ep = Episode()
        //getUrl("anyanime", ep)
    }
    override fun onStart() {
        super.onStart()
        val adaptiveTrackSelection: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultRenderersFactory(this),
            DefaultTrackSelector(adaptiveTrackSelection), DefaultLoadControl()
        )
        playerView.player = player
    }
    private fun releaseVod(url:String) {
        VodActivity.fullScreenVideoWebView.loadUrl("")
        fullScreenVideoWebView.visibility = View.GONE
        vod=url
        playerView.visibility=View.VISIBLE
        defaultBandwidthMeter = DefaultBandwidthMeter()
        dataSourceFactory= DefaultDataSourceFactory(this, Util.getUserAgent(this, "Exo2"), defaultBandwidthMeter)
        val hls_url  = url
        val uri = Uri.parse(hls_url)
        if(vod.contains("m3u8")){
            mediaSource = HlsMediaSource
                .Factory(dataSourceFactory)
                .createMediaSource(uri)
        }else{
            mediaSource = ExtractorMediaSource
                .Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
        player.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {}
            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState)
                {
                    ExoPlayer.STATE_READY -> {
                        checkFinish=true
                        loading1.visibility = View.GONE
                    }
                    ExoPlayer.STATE_BUFFERING -> loading1.visibility = View.VISIBLE
                }
            }
            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: ExoPlaybackException) {}
            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
        resumeState()
        playerView.setOnClickListener{
            episodeRecycler.visibility=View.GONE
        }
    }
    private fun releasePlayer() {
        player.stop()
        playbackPosition = player.currentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }
    private fun getUrl(sourceName : String, episode: Episode) {
        var badServer:List<Server> = listOf()
        var goodServer:List<Server> = listOf()
        var servers :List<Server> = listOf()
        var compteur=0
        var check=0
        val tread =Thread(Runnable {
            check = 1
            try {
                if (sourceName == "voiranime"){
                    val doc = Jsoup.connect(episode.url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("iframe")
                    val server = Server()
                    server.name = "gounlimited"
                    server.url = elements.toString().substringAfter("src=\"").substringBefore("\"")
                    episode.servers = episode.servers.plus(server)
                    var bool = true
                    for (s in episode.servers){
                        if (bool&&getMp4(s)){
                            bool = false
                            goodServer = goodServer.plus(justserver)
                        }else{
                            badServer = badServer.plus(s)
                        }
                    }
                    goodServer = goodServer.plus(badServer)
                }
                else if (sourceName == "vostfree"){
                    for (server in episode.servers){
                        goodServer = goodServer.plus(server)
                    }
                }
                else if (sourceName == "gogoanime"){
                    val doc: Document = Jsoup.connect(episode.url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements=doc.select("div.anime_muti_link").select("li")
                    for(element in elements){
                        val s = Server()
                        s.name = element.text().substringBefore("Choose")
                        s.url =  element.toString().substringAfter("data-video=\"").substringBefore("\"").substringBefore(";")
                        if (!s.url.contains("https")) s.url = "https:"+s.url
                        episode.servers = episode.servers.plus(s)
                    }
                    var bool = true
                    for (s in episode.servers){
                        if (bool&&getMp4(s)){
                            bool = false
                            goodServer = goodServer.plus(justserver)
                        }else{
                            badServer = badServer.plus(s)
                        }
                    }
                    goodServer = goodServer.plus(badServer)
                }
                else if (sourceName == "otakufr"){
                    var doc = Jsoup.connect(episode.url)
                        .timeout(0).validateTLSCertificates(false)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("div.nav_ver").select("a")
                    var bool = true
                    for (element in elements){
                        if (!bool) break
                        val server = Server()
                        server.name = element.text()
                        server.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        doc = Jsoup.connect(server.url)
                            .timeout(0).validateTLSCertificates(false)
                            .maxBodySize(1024 * 1024 * 10)
                            .get()
                        val elts = doc.select("div.vdo_wrp").select("iframe")
                        server.url = elts.toString().substringAfter("src=\"").substringBefore("\"")
                        if (!server.url.contains("https://"))server.url="https://"+server.url
                        server.name = server.name
                        if (bool&&getMp4(server)){
                            bool = false
                            goodServer = goodServer.plus(justserver)
                        }else{
                            badServer = badServer.plus(server)
                        }
                    }
                    goodServer = goodServer.plus(badServer)
                }
                else if (sourceName == "animedao"){
                    val doc = Jsoup.connect(episode.url)
                        .timeout(0).validateTLSCertificates(false)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("script")
                    for (element in elements){
                        val str = element.toString()
                        if (str.contains("function")&&str.contains("src=\""))
                        {
                            val server = Server()
                            server.name = str.substringAfter("function").substringBefore("(")
                            server.url ="https://animedao.com"+ str.substringAfter("src=\"").substringBefore("\"")
                            episode.servers = episode.servers.plus(server)
                        }

                    }
                    var bool = true
                    for (s in episode.servers){
                        if (bool&&getMp4(s)){
                            bool = false
                            goodServer = goodServer.plus(justserver)
                        }else{
                            badServer = badServer.plus(s)
                        }
                    }
                    goodServer = goodServer.plus(badServer)
                }
                else if (sourceName == "anyanime"){
                    val doc = Jsoup.connect(episode.url)
                        .timeout(0).validateTLSCertificates(false)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("div.anime-online").select("div.iframe-show").select("a.down_get")
                    for (element in elements){
                        val str = element.toString()
                        val server = Server()
                        server.url = element.toString().substringAfter("adf.ly/").substringAfter("/").substringBefore("\"")
                        if (server.url.contains("gounlimited")){
                            server.url = "https://gounlimited.to/embed-"+server.url.substringAfter("to/").substringBefore("/")+".html"
                            server.name = "gounlimited"
                        } else if (server.url.contains("dood.to")){
                            server.name = "dood"
                        } else if (server.url.contains("uptobox")){
                            server.name = "uptobox"
                        } else if (server.url.contains("mp4upload")){
                            server.name = "mp4upload"
                        }else if (server.url.contains("samaup")){
                            server.name = "samaup"
                        }
                        if (server.name !="")episode.servers = episode.servers.plus(server)
                    }
                    var bool = true
                    for (s in episode.servers){
                        if (bool&&getMp4(s)){
                            bool = false
                            goodServer = goodServer.plus(justserver)
                        }else{
                            badServer = badServer.plus(s)
                        }
                    }
                    goodServer = goodServer.plus(badServer)
                    val s = Server()
                    s.name = sourceName
                    s.url = episode.url
                    goodServer = goodServer.plus(s)
                }
                else if (sourceName == "animeflv"){
                    val doc = Jsoup.connect("https://animeflv.net/ver/naruto-220")
                        .timeout(0).validateTLSCertificates(false)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("script")
                    for (element in elements){
                        var str = element.toString()
                        if (str.contains("anime_id"))
                        {
                            str = str.substringAfter("title\":\"")
                            while (str.contains("title\":\"")){
                                val server = Server()
                                server.name = str.substringBefore("\"")
                                server.url = str.substringAfter("code\":\"").substringBefore("\"")
                                episode.servers = episode.servers.plus(server)
                                str = str.substringAfter("title\":\"")
                            }
                            break
                        }
                    }
                }
                else if (sourceName == "jkanime"){
                    val doc = Jsoup.connect(episode.url)
                        .timeout(0).validateTLSCertificates(false)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("script")
                    for (element in elements){
                        var str = element.toString()
                        if (str.contains("var video"))
                        {
                            str = str.substringAfter("src=\"")
                            while (str.contains("src=\"")){
                                val server = Server()
                                server.name = "jkanime"
                                server.url = str.substringBefore("\"")
                                goodServer = goodServer.plus(server)
                                str = str.substringAfter("src=\"")
                            }
                        }
                    }
                }
                else if (sourceName == "animebum"){
                    val doc = Jsoup.connect("https://animebum.net/v/tenki-no-ko-sub-espanol")
                        .timeout(0).validateTLSCertificates(false)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("script")
                    for (element in elements){
                        var str = element.toString()
                        if (str.contains("<iframe src"))
                        {
                            str = str.substringAfter("src=\"")
                            while (str.contains("src=\"")){
                                val server = Server()
                                server.url = str.substringBefore("\"")
                                episode.servers = episode.servers.plus(server)
                                str = str.substringAfter("src=\"")
                            }
                            break
                        }
                    }
                }
                else if (sourceName == "ianime"){
                    val server = Server()
                    server.name = "kkkkkkkkk"
                    server.url = episode.url
                    goodServer = goodServer.plus(server)
                }
                else if (sourceName == "animefreak"){
                    val doc = Jsoup.connect(episode.url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("div.sd-nav").select("a")
                    for (element in elements){
                        val server = Server()
                        server.name = element.text()
                        server.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                        episode.servers = episode.servers.plus(server)
                    }
                    var bool = true
                    var i = 0
                    for (s in episode.servers){
                        if (bool&&getMp4(s)){
                            if(i==1) bool = false
                            i++
                            goodServer = goodServer.plus(justserver)
                        }else{
                            badServer = badServer.plus(s)
                        }
                    }
                    goodServer = goodServer.plus(badServer)
                }
                else if (sourceName == "animefenix"){
                    val doc = Jsoup.connect(episode.url)
                        .timeout(0)
                        .maxBodySize(1024 * 1024 * 10)
                        .get()
                    val elements = doc.select("div.player-container").select("script")
                    var str = elements.toString().substringAfter("src='")
                    while (str.contains("src='")){
                        val s = str.substringBefore("src='")
                        val server = Server()
                        server.url = s.substringBefore("'")
                        if (server.url.contains("..")) server.url = server.url.replace("..","https://www.animefenix.com")
                        if (!server.url.contains("http")) server.url = "https://animefenix.com" + server.url
                        if (server.url.contains("yourupload")) server.name = "yourupload"
                        else if (server.url.contains("sendvid")) server.name = "sendvid"
                        else if (server.url.contains("mp4upload")) {
                            str = str.substringAfter("src='")
                            continue
                        }
                        else if (server.url.contains("upvid")) server.name = "upvid"
                        else server.name = "server"
                        if (server.url.contains("&amp;")) server.url = server.url.replace("&amp;","&")
                        str = str.substringAfter("src='")
                        episode.servers = episode.servers.plus(server)
                    }
                    var bool = true
                    for (s in episode.servers){
                        if (bool&&getMp4(s)){
                            bool = false
                            goodServer = goodServer.plus(justserver)
                        }else{
                            badServer = badServer.plus(s)
                        }
                    }
                    goodServer = goodServer.plus(badServer)
                }
                var i = 0
                while (i<goodServer.size){
                    var i2 = 0
                    var bool = true
                    while (i2<servers.size){
                        if (servers[i2].name==goodServer[i].name&&servers[i2].url==goodServer[i].url){
                            bool = false
                            break
                        }
                        i2++
                    }
                    if (bool) servers =servers.plus(goodServer[i])
                    i++
                }
                goodServer = servers
                for (s in goodServer){
                }
            }
            catch (e:Exception){ }
        })
        tread.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (check==1&&goodServer.isNotEmpty()) {
                    serverRecycler.adapter = AdapterServerVod(this@VodActivity,goodServer)
                    if (goodServer[0].url.contains("linkkader")||goodServer[0].url.contains("mp4")||goodServer[0].url.contains("m3u")){
                        releaseVod(goodServer[0].url.replace("linkkader",""))
                    }else {
                        serverRecycler.visibility=View.VISIBLE
                    }
                    check=0
                }
                else{
                    if (compteur==120){
                        if (goodServer.isEmpty()){
                            Toast.makeText(this@VodActivity,"Aucun server disponible",Toast.LENGTH_LONG).show()
                        }
                        this@VodActivity.finish()
                    }
                    compteur++
                    mainHandler.postDelayed(this, 1000)
                }
            }
        })
    }
    fun getMp4(server : Server): Boolean {
        var bool = false
        if (server.url.contains("sendvid")||server.url.contains("streamango")||server.url.contains("openload")
            ||server.url.contains("drive.googl")||server.url.contains("streamz")||server.url.contains("uqload")
            ||server.url.contains("upvid")||server.url.contains("mega")
            ||server.url.contains("fembed")||server.url.contains("yourupload")){
            return false
        }
        var doc = Document.createShell("")
        try {
            doc = Jsoup.connect(server.url)
                    .timeout(0).validateTLSCertificates(false)
                    .maxBodySize(1024 * 1024 * 10)
                    .get()
        }catch (e:Exception){ }
        server.url = doc.location()
        justserver = server
        if (server.url.contains("animefreak")){
            val elements = doc.select("div.vmn-video")
            server.url=elements.toString().substringAfter("var file = \"").substringBefore("\"").replace(" ","%20")
            if (server.url.contains("1080")) server.name = server.name + "1080"
            else if (server.url.contains("1080")) server.name = server.name + "720"
            justserver =server
            bool=true
        }else if (server.url.contains("animefenix")){
            server.url = doc.toString().substringAfter("\"file\":\"").substringBefore("\"").replace("\",","")+"linkkader"
            server.name = "animefenix"
            justserver =server
            bool=true
        }else if (doc.toString().contains("gounlimited"))
        {
            val elements=doc.select("script")
            for (element in elements){
                if (element.toString().contains("eval(")&&element.toString().contains("mp4")){
                    server.url="https://"+element
                        .toString().substringBeforeLast("'.")
                        .substringAfterLast("|")+".gounlimited.to/"+elements[elements.size-2].toString()
                        .substringBeforeLast("|")
                        .substringBeforeLast("|")
                        .substringAfterLast("|").replace(" ","") + "/v.mp4"
                    server.name="gounlimited"
                    justserver=server
                    if (!server.url.contains("small"))bool= true
                }
            }
        }
        /*else if (doc.toString().contains("mp4upload"))
        {
            val elements=doc.select("script")
            server.name="mp4upload"
            for (element in elements ){
                if (element.toString().contains("eval(")) {
                    val str = element.toString()
                    server.url = "http://"+str.substringAfter("type|").substringBefore("|")
                    server.url+="."+str.substringAfter("'|").substringAfter("|").substringBefore("|")+"."+str.substringAfter("'|").substringBefore("|")
                    server.url += ":"+str.substringBefore("|sources").substringAfterLast("|")+"/d/"+str.substringAfter("getElementById|").substringBefore("|")
                    server.url += "/video.mp4"
                    break
                }
            }
            justserver=server
            if (justserver.url.contains("mp4")) bool= true
        }
        */
        else if (doc.toString().contains("allvid")){
            val elements=doc.select("script")
            if (elements.toString().contains("mp4")){
                server.url=elements.toString().substringAfter("file:\"").substringBefore("\"").replace(" ","")
                server.name="allvid"
                justserver =server
                bool=true
            }
        }
        else if (doc.toString().contains("upstream")){
            val elements=doc.select("script")
            if (elements.toString().contains("mp4")){
                server.url=elements.toString().substringAfter("file:\"").substringBefore("\"").replace(" ","")
                server.name="upstream"
                justserver = server
                if(!server.url.contains("small")) bool=true
            }
        }
        else if (doc.toString().contains("vidstreaming")&&doc.toString().contains("file: '")){
            val elements=doc.select("div.videocontent")
            server.url=elements.toString().substringAfter("file: '").substringAfter("file: '").substringBefore("'")
            server.name = "vidstreaming"
            justserver = server
            bool = true
        }
        return bool
    }
    override fun onPause() {
        if (vod!=""){
            saveState()
        }
        super.onPause()
    }
    private fun resumeState(){
        val a= getSharedPreferences(FragmentAnimeInfo.str, Context.MODE_PRIVATE)
        val b=a.getLong(position.toString(),0)
        player.playWhenReady = true
        player.prepare(mediaSource)
        player.seekTo(b)
    }
    override fun onResume() {
        player.stop()
        if (vod!=""){
            resumeState()
        }
        super.onResume()
    }
    override fun onStop() {
        releasePlayer()
        super.onStop()
    }
    override fun onDestroy() {
        if (vod!=""){
            saveState()
        }
        releasePlayer()
        super.onDestroy()
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

}
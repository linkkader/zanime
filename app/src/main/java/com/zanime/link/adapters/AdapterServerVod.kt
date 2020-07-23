package com.zanime.link.adapters

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.type.Server
import com.zanime.link.ui.MainActivity
import com.zanime.link.ui.VodActivity
import com.zanime.link.ui.VodActivity.Companion.fullScreenVideoWebView
import com.zanime.link.ui.VodActivity.Companion.loading1
import com.zanime.link.ui.VodActivity.Companion.player
import com.zanime.link.ui.VodActivity.Companion.playerView


class AdapterServerVod(var context : Context?, var servers : List<Server>) : RecyclerView.Adapter<AdapterServerVod.ViewHolder>() {

    var vod = ""
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout : LinearLayout = itemView.findViewById(R.id.mainLayout)
        val episodeTitle = itemView.findViewById<TextView>(R.id.episodeTitle)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.item_episode,parent,false))
    }
    override fun getItemCount(): Int {
        return  servers.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (servers[position].url.contains("linkkader")||servers[position].url.contains("mp4")||servers[position].url.contains("m3u")){
            holder.episodeTitle.text = servers[position].name
        }else holder.episodeTitle.text = servers[position].name + "externe"
        holder.linearLayout.background = context!!.getDrawable(R.drawable.noback)
        holder.itemView.setOnClickListener {
            if (!verifyAvailableNetwork(context!!.applicationContext)){
                Toast.makeText(context, context!!.getString(R.string.verify_internet),
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            VodActivity.saveState()
            if (UnityAds.isReady("inters")&& MainActivity.bool){
                UnityAds.show(context as Activity?,"inters")
            }
            if (servers[position].url.contains("mp4")||servers[position].url.contains("m3u")) {
                releaseVod(servers[position].url)
            }else{
                try {
                    player.stop()
                    playerView.visibility=View.GONE
                    fullScreenVideoWebView.loadUrl(servers[position].url)
                    var i = 0
                    loading1.visibility=View.VISIBLE
                    fullScreenVideoWebView.visibility=View.GONE
                    var bool = false
                    Handler().postDelayed({
                        loading1.visibility=View.GONE
                        fullScreenVideoWebView.visibility=View.VISIBLE
                        bool = true
                    },5000)
                    fullScreenVideoWebView.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                            if (i==1) bool = true
                            i++
                            return bool
                        }
                    }
                    fullScreenVideoWebView.visibility=View.VISIBLE
                }catch (e:Exception){}
            }
        }
    }

    private fun releaseVod(url:String) {
        VodActivity.fullScreenVideoWebView.loadUrl("")
        fullScreenVideoWebView.visibility = View.GONE
        vod=url
        VodActivity.playerView.visibility=View.VISIBLE
        VodActivity.defaultBandwidthMeter = DefaultBandwidthMeter()
        VodActivity.dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context!!.applicationContext, "Exo2"), VodActivity.defaultBandwidthMeter)
        val hls_url  = url
        val uri = Uri.parse(hls_url)
        if(vod.contains("m3u8")){
            VodActivity.mediaSource = HlsMediaSource
                .Factory(VodActivity.dataSourceFactory)
                .createMediaSource(uri)
        }else{
            VodActivity.mediaSource = ExtractorMediaSource
                .Factory(VodActivity.dataSourceFactory)
                .createMediaSource(uri)
        }
        VodActivity.player.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {}
            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState)
                {
                    ExoPlayer.STATE_READY -> {
                        VodActivity.checkFinish =true
                        VodActivity.loading1.visibility = View.GONE
                    }
                    ExoPlayer.STATE_BUFFERING -> VodActivity.loading1.visibility = View.VISIBLE
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
        VodActivity.playerView.setOnClickListener{
            VodActivity.episodeRecycler.visibility=View.GONE
        }
    }

    private fun resumeState(){
        val a= context!!.getSharedPreferences("", Context.MODE_PRIVATE)
        val b=a.getLong(VodActivity.position.toString(),0)
        VodActivity.player.playWhenReady = true
        VodActivity.player.prepare(VodActivity.mediaSource)
        VodActivity.player.seekTo(b)
    }
    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
    fun verifyAvailableNetwork(activity: Context):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}

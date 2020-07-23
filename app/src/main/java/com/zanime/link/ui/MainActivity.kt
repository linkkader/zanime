package com.zanime.link.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.unity3d.ads.IUnityAdsListener
import com.unity3d.ads.UnityAds
import com.zanime.link.R
import com.zanime.link.ui.fragment.FragmentMain
import com.zanime.link.ui.fragment.FragmentSearch
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var recycler : RecyclerView
        var bool = true
    }
    lateinit var container : FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        container=findViewById(R.id.container_main)
        supportFragmentManager.beginTransaction().add(container.id, FragmentMain()).commit()
        var text = ""
        var threadCheck = 0
        supportActionBar?.hide()
        val testMode = false
        UnityAds.initialize(this,"3689991", testMode)
        val unityAdListener=object : IUnityAdsListener {
            override fun onUnityAdsStart(p0: String?) {}
            override fun onUnityAdsFinish(p0: String?, p1: UnityAds.FinishState?) {
                if (bool){
                    bool=false
                    Handler().postDelayed({
                        UnityAds.load("inters")
                        bool = true
                        Handler().postDelayed({
                            bool = true
                        },1000*60)
                    },14*60*1000)
                }
            }
            override fun onUnityAdsError(p0: UnityAds.UnityAdsError?, p1: String?) {
                Handler().postDelayed({
                    UnityAds.load("inters")
                    Handler().postDelayed({
                        bool = true
                    },1000*60)
                },7*60*1000)
            }
            override fun onUnityAdsReady(p0: String?) { }
        }
        UnityAds.setListener(unityAdListener)
        UnityAds.load("inters")


        val tread =Thread(Runnable {
            try {
                val doc: Document = Jsoup.connect("https://rebrand.ly/linkkaderzanimeupdate")
                    .timeout(0)
                    .followRedirects(true)
                    .maxBodySize(1024 * 1024 * 10)
                    .get()
                text = doc.getElementById("viewPost").text()
                threadCheck=1
            }catch (e:Exception){ }
        })
        tread.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (threadCheck==1) {
                    if (text.contains("allow")){
                        container.visibility = View.VISIBLE
                        supportActionBar?.show()
                         }
                    if (text.contains("youtube")){
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(text.substringAfter(":"))))
                        }catch (e:Exception){}
                    }
                    if (text.contains("hello")){
                        try {
                            Toast.makeText(this@MainActivity,text.substringAfter("hello").substringBefore("hello"),Toast.LENGTH_LONG).show()
                        }catch (e:Exception){}
                    }
                    if (text.contains("update")){
                        container.visibility = View.GONE
                        supportActionBar?.hide()
                        card_update.visibility = View.VISIBLE
                    }
                }
                else{
                    if (!verifyAvailableNetwork(this@MainActivity)) {
                        Toast.makeText(this@MainActivity,this@MainActivity.getString(R.string.verify_internet),Toast.LENGTH_LONG).show()
                    }
                    mainHandler.postDelayed(this, 1000)
                }
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater= menuInflater
        inflater.inflate(R.menu.bar,menu)
        val manager= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem= menu?.findItem(R.id.search)
        menu?.findItem(R.id.youtube)?.setOnMenuItemClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.youtube.com/channel/UCfaWpxEqWpvKchrunVzL9eA")))
            return@setOnMenuItemClickListener true
        }
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchItem.collapseActionView()
                if (query != null ) {
                    if (verifyAvailableNetwork(this@MainActivity)){
                        if (supportFragmentManager.findFragmentByTag("searchitem")!=null){
                            supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentByTag("searchitem")!!).commit()
                        }
                        supportFragmentManager.beginTransaction().add(R.id.container_main,FragmentSearch(query.replace(" ","%20")),"searchitem").addToBackStack(null).commit()
                    } else {
                        Toast.makeText(this@MainActivity,this@MainActivity.getString(R.string.verify_internet),Toast.LENGTH_LONG).show()
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    fun onShadowClickTest(view: View) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://rebrand.ly/linkkaderzanime")))
        }catch (e:Exception){}
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }
}
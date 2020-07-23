package com.zanime.link.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.zanime.link.R
import com.zanime.link.VewPagerAdapter

class AnimeInfo : AppCompatActivity() {
    companion object{

        lateinit var fa: Activity
        lateinit var supportBar : androidx.appcompat.app.ActionBar
        var name:String =""
        var url:String=""
        var source:String=""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_info)
        supportBar  = this.supportActionBar!!
        supportBar.title = name
        fa = this
        val viewPager = findViewById<ViewPager>(R.id.pager)
        viewPager.adapter = VewPagerAdapter(supportFragmentManager,url,source)
    }
}
package com.zanime.link

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zanime.link.ui.fragment.FragmentAnimeInfo
import com.zanime.link.ui.fragment.FragmentEpisode

class VewPagerAdapter(var fragment: FragmentManager,val url : String,val source:String) : FragmentStatePagerAdapter(fragment){
    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                FragmentAnimeInfo(url,source)
            }
            1->{
                FragmentEpisode()
            }
            else -> FragmentEpisode()

        }
    }
    override fun getCount(): Int {
        return 2
    }
    override fun getPageTitle(position: Int): String {
        return when(position){
            0-> "Info"
            else -> "Episodes"
        }

    }
}

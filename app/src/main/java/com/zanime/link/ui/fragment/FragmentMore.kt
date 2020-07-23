package com.zanime.link.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterBrowseCheck
import com.zanime.link.type.Source


class FragmentMore : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var list = listOf<Source>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insert("voiranime","https://cdn.voiranime.to/wp-content/uploads/fbrfg/apple-touch-icon.png","Fr")
        insert("gogoanime","https://cdn.gogocdn.net/files/gogo/img/favicon.png","En")
        insert("otakufr","https://www.otakufr.com/favicon.png","french")
        insert("vostfree","https://vostfree.com/templates/Animix/images/logo.png","Fr")
        insert("animedao","https://animedao.com/img/animedaofb.png","En")
        insert("ianime","https://www.ianimes.org/img/i-anime.png","Fr")
        insert("animefreak","https://alternative.app/data/original/AnimeFreakPFDgTr.png","En")
        insert("animefenix","https://www.animefenix.com/themes/animefenix-frans185/images/AveFenix.png","Es")
        insert("jkanime","https://cdn.jkanime.net/assets/images/logo.png","Es")
        insert("anyanime","https://ww3.anyanime.com/wp-content/uploads/2015/11/Anyanime-logo1.png","Ar")
        val recycler = view.findViewById<RecyclerView>(R.id.double_list)
        view.findViewById<AppCompatImageView>(R.id.github).setOnClickListener{
            try {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://rebrand.ly/linkkadergithubvoiranime")))
            }catch (e:Exception){}
        }
        view.findViewById<AppCompatImageView>(R.id.discord).setOnClickListener{
            try {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://rebrand.ly/linkkaderdiscord1")))
            }catch (e:Exception){}
        }
        view.findViewById<AppCompatImageView>(R.id.appCompatImageView).setOnClickListener{
            try {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://rebrand.ly/linkkaderyoutube")))
            }catch (e:Exception){}
        }
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = AdapterBrowseCheck(context,list,activity!!.supportFragmentManager)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }
    fun insert(name:String,img:String,language : String){
        val source  = Source(name,"")
        source.img = img
        source.language = language
        list = list.plus(source)
    }
}
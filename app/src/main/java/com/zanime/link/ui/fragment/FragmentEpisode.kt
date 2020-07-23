package com.zanime.link.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterEpisode
import com.zanime.link.database.history.HistoryViewModel


class FragmentEpisode : Fragment() {
    companion object{
        lateinit var listViewModel : HistoryViewModel
        lateinit var recyclerView : RecyclerView
        lateinit var thisAdapter : AdapterEpisode
        lateinit var resume : TextView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resume = view.findViewById(R.id.resume)
        recyclerView = view.findViewById(R.id.recycler_episode)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext)
        listViewModel = ViewModelProvider(this@FragmentEpisode).get(HistoryViewModel(activity!!.application)::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }
}
package com.zanime.link.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterHistory
import com.zanime.link.database.history.HistoryViewModel


class FragmentHistory : Fragment() {
    companion object{
        lateinit var listViewModel : HistoryViewModel
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler : RecyclerView = view.findViewById(R.id.recycler_history)
        val linearLayoutManager =LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recycler.layoutManager = linearLayoutManager

        val adapter = AdapterHistory(activity)
        recycler.adapter = adapter
        listViewModel = ViewModelProvider(this@FragmentHistory).get(HistoryViewModel(activity!!.application)::class.java)
        listViewModel.all.observe(viewLifecycleOwner , Observer { lists ->
            for (l in lists)
            lists.let { adapter.setHistorys(lists) }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

}
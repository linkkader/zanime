package com.zanime.link.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanime.link.R
import com.zanime.link.adapters.AdapterLibrary
import com.zanime.link.database.library.LibraryViewModel


class FragmentLibrary : Fragment() {
    lateinit var listViewModel : LibraryViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler : RecyclerView = view.findViewById(R.id.recycler_library)
        val gridLayoutManager=GridLayoutManager(context,2)
        recycler.layoutManager = gridLayoutManager
        val adapter = AdapterLibrary(activity)
        recycler.adapter = adapter
        listViewModel = ViewModelProvider(this@FragmentLibrary).get(LibraryViewModel(activity!!.application)::class.java)
        listViewModel.allWords.observe(viewLifecycleOwner , Observer { lists ->
            lists.let { adapter.setLibrarys(lists) }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

}
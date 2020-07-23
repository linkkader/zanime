package com.zanime.link.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zanime.link.R

class FragmentMain : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val container = view.findViewById<FrameLayout>(R.id.container)
        val transaction1 = activity!!.supportFragmentManager.beginTransaction()
        transaction1.add(R.id.container,FragmentMore(),"info")
        transaction1.add(R.id.container,FragmentHistory(),"history")
        transaction1.add(R.id.container,FragmentLibrary(),"library").commit()

        val nav : BottomNavigationView = view.findViewById(R.id.bottom_nav)
        nav.setOnNavigationItemSelectedListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            when(it.itemId) {
                R.id.nav_library -> {
                    if (activity!!.supportFragmentManager.findFragmentByTag("library")!= null){
                        hide()
                        transaction.show(activity!!.supportFragmentManager.findFragmentByTag("library")!!)
                        transaction.commit()
                        return@setOnNavigationItemSelectedListener true
                    }
                    transaction.add(R.id.container,FragmentLibrary(),"library").commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_history -> {
                    if (activity!!.supportFragmentManager.findFragmentByTag("history")!= null){
                        hide()
                        transaction.show(activity!!.supportFragmentManager.findFragmentByTag("history")!!)
                        transaction.commit()
                        return@setOnNavigationItemSelectedListener true
                    }
                    transaction.add(R.id.container,FragmentHistory(),"history").commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_browse ->{
                    if (activity!!.supportFragmentManager.findFragmentByTag("browse")!= null){
                        hide()
                    }
                    transaction.add(R.id.container,FragmentBrowser(),"browse").commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_more ->{
                    if (activity!!.supportFragmentManager.findFragmentByTag("info")!= null){
                        hide()
                        transaction.show(activity!!.supportFragmentManager.findFragmentByTag("info")!!)
                        transaction.commit()
                        return@setOnNavigationItemSelectedListener true
                    }
                    transaction.add(R.id.container,FragmentMore(),"info").commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener true
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    fun hide() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        if (activity!!.supportFragmentManager.findFragmentByTag("info")!=null){
            transaction.hide(activity!!.supportFragmentManager.findFragmentByTag("info")!!)
        }
        if (activity!!.supportFragmentManager.findFragmentByTag("library")!=null){
            transaction.hide(activity!!.supportFragmentManager.findFragmentByTag("library")!!)
        }
        if (activity!!.supportFragmentManager.findFragmentByTag("history")!=null){
            transaction.hide(activity!!.supportFragmentManager.findFragmentByTag("history")!!)
        }
        if (activity!!.supportFragmentManager.findFragmentByTag("browse")!=null){
            transaction.hide(activity!!.supportFragmentManager.findFragmentByTag("browse")!!)
        }
        transaction.commit()
    }
}
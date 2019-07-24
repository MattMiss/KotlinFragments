package com.mattmiss.kotlinfragments

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    // Creates a drawToggle variable
    private val drawerToogle by lazy {
        ActionBarDrawerToggle(this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentA = FragmentSearchFood.newInstance()
        replaceFragment(fragmentA)

        // set the toolbar
        setSupportActionBar(toolbar)

        // set navigation panel click listener
        navigationView.setNavigationItemSelectedListener {
            selectDrawerItem(it)
            true
        }

        // Add a toggle listener to the menu drawer
        drawerLayout.addDrawerListener(drawerToogle)



    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToogle.onConfigurationChanged(newConfig)
    }

    private fun selectDrawerItem(item: MenuItem) {
        var fragment: androidx.fragment.app.Fragment? = null
        val fragmentClass = when (item.itemId) {
            R.id.firstFragmentItem -> FragmentSearchFood::class.java
            R.id.secondFragmentItem -> FragmentMyFood::class.java
            else -> FragmentSearchFood::class.java
        }
        try {
            fragment = fragmentClass.newInstance() as androidx.fragment.app.Fragment
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        replaceFragment(fragment)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (drawerToogle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fragment_menu, menu)
        return true
    }


    private fun replaceFragment(fragment: androidx.fragment.app.Fragment?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // changed to fragment!! for it to work
        fragmentTransaction.replace(R.id.fragmentContainer, fragment!!)
        fragmentTransaction.commit()


    }


}

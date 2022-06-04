package com.wordyka.kantinkorperasiitdel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wordyka.kantinkorperasiitdel.activity.koperasi.MainAdminActivity
import com.wordyka.kantinkorperasiitdel.activity.MasukActivity
import com.wordyka.kantinkorperasiitdel.activity.kantin.MainAdminKantinActivity
import com.wordyka.kantinkorperasiitdel.fragment.AkunFragment
import com.wordyka.kantinkorperasiitdel.fragment.HomeFragment
import com.wordyka.kantinkorperasiitdel.fragment.KeranjangFragment
import com.wordyka.kantinkorperasiitdel.helper.SharePref

class MainActivity : AppCompatActivity() {

    val fragmentHome: Fragment = HomeFragment()
    val fragmentKeranjang = KeranjangFragment()
    val fragmentAkun: Fragment = AkunFragment()
    val fm:FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome

    private lateinit var menu : Menu
    private lateinit var menuItem: MenuItem
    private lateinit var BottomNavigationView: BottomNavigationView
    private val statusLogin = false

    private lateinit var sp:SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        sp = SharePref(this)

        if(sp.getUser()?.role?.equals("admin-koperasi", ignoreCase = true)!!) {
            startActivity(Intent(this@MainActivity, MainAdminActivity::class.java))
        } else if(sp.getUser()?.role?.equals("admin-kantin", ignoreCase = true)!!) {
            startActivity(Intent(this@MainActivity, MainAdminKantinActivity::class.java))
        }else if(sp.getUser()?.role?.equals(null)!!) {
            setContentView(R.layout.activity_main)
            setUpBottomNav()
        } else {
            setContentView(R.layout.activity_main)
            setUpBottomNav()
        }

    }



    fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        BottomNavigationView = findViewById(R.id.nav_view)
        menu = BottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        BottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    callFragment(0, fragmentHome)
                }
                R.id.navigation_keranjang -> {
                    callFragment(1, fragmentKeranjang)
                }
                R.id.navigation_akun -> {
                    if(sp.getStatusLogin()) {
                        callFragment(2, fragmentAkun)
                    } else {
                        startActivity(Intent(this,MasukActivity::class.java))
                    }
                }
            }

            false
        }
    }

    fun callFragment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}
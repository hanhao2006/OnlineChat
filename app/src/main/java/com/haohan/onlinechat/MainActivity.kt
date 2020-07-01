package com.haohan.onlinechat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.haohan.onlinechat.Fragments.ChatFragment
import com.haohan.onlinechat.Fragments.SearchFragment
import com.haohan.onlinechat.Fragments.SettingFragment
import com.haohan.onlinechat.Fragments.ViewPagerAdapter

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = ""

        val tablayout: TabLayout = findViewById(R.id.tablelayout)
        val viewPager: ViewPager = findViewById(R.id.view_pager)

        val viewPageAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPageAdapter.addFragment(ChatFragment(),"Chat")
        viewPageAdapter.addFragment(SearchFragment(),"Search")
        viewPageAdapter.addFragment(SettingFragment(),"Setting")

        viewPager.adapter = viewPageAdapter
        tablayout.setupWithViewPager(viewPager)

        mAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_signOut -> {
                mAuth.signOut()
                startActivity(Intent(this@MainActivity,WelcomeActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

}

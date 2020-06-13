package com.rasyidin.githubuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.adapter.DetailUserAdapter
import com.rasyidin.githubuser.adapter.SectionsPagerAdapter
import com.rasyidin.githubuser.model.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tab_layout.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailAdapter: DetailUserAdapter

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initToolbar()

        detailAdapter = DetailUserAdapter()
        rvDetailActivity.layoutManager = LinearLayoutManager(this)
        rvDetailActivity.adapter = detailAdapter

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        detailViewModel.setUserDetail(username!!)
        detailViewModel.getDetailUser().observe(this, Observer { detailItemUser ->
            if (detailItemUser != null) {
                detailAdapter.setData(detailItemUser)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionChangeLanguageSetting) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.titleActionbarDetail)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

}
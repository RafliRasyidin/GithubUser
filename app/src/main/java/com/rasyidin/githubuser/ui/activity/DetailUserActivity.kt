package com.rasyidin.githubuser.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.adapter.DetailUserAdapter
import com.rasyidin.githubuser.adapter.SectionsPagerAdapter
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.rasyidin.githubuser.model.User
import com.rasyidin.githubuser.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tab_layout.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailAdapter: DetailUserAdapter
    private lateinit var uriWithId: Uri
    private var user: User? = null
    private var isFavorite = false

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

        user = intent.getParcelableExtra(EXTRA_USERNAME) as User

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        detailViewModel.setUserDetail(user!!.login!!)
        detailViewModel.getDetailUser().observe(this, Observer { detailItemUser ->
            if (detailItemUser != null) {
                detailAdapter.setData(detailItemUser)
            }
        })

        setFavorite()
    }

    private fun setStatusFavorite(state: Boolean) {
        if (state) {
            fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.baseline_favorite_white_36
                )
            )
        } else {
            fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.baseline_favorite_border_white_36
                )
            )
        }
    }

    private fun setFavorite() {
        fabFavorite.setOnClickListener {
            if (isFavorite) {
                user?.let {
                    contentResolver.delete(uriWithId, null, null)
                    Toast.makeText(this, "${it.login} Removed form Favorite", Toast.LENGTH_SHORT)
                        .show()
                    isFavorite = false
                    setStatusFavorite(isFavorite)
                }
            } else {
                val values = ContentValues()
                values.put(USERNAME, user?.login)
                values.put(AVATAR_URL, user?.avatars)
                values.put(TYPE, user?.type)
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, "${user?.login} Added to Favorite", Toast.LENGTH_SHORT).show()
                isFavorite = true
                setStatusFavorite(true)
            }
        }

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
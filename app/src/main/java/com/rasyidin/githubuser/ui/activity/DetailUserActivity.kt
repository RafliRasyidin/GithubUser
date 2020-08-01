package com.rasyidin.githubuser.ui.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.rasyidin.githubuser.database.DatabaseContract.CONTENT_URI
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.rasyidin.githubuser.model.User
import com.rasyidin.githubuser.viewmodel.DetailViewModel
import com.rasyidin.githubuser.widget.FavoriteWidget
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tab_layout.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailAdapter: DetailUserAdapter
    private lateinit var uriWithUsername: Uri
    private var user: User? = null
    private var isFavorite = false

    companion object {
        internal val TAG = DetailUserActivity::class.java.simpleName
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.title = resources.getString(R.string.title_actionbar_detail)

        showRecyclerList()
        setPagerAdapter()

        user = intent.getParcelableExtra(EXTRA_USERNAME) as User

        uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + user?.login)
        isFavorited()
        setDetail()
        setFavorite()
    }

    private fun setFabFavorite(state: Boolean) {
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
                    contentResolver.delete(uriWithUsername, null, null)
                    Log.d(TAG, "Removed Success")
                    Toast.makeText(this, "${it.login} Removed form Favorite", Toast.LENGTH_SHORT)
                        .show()
                    isFavorite = false
                    sendUpdateFavoriteList(this)
                    setFabFavorite(isFavorite)
                }
            } else {
                val values = ContentValues().apply {
                    put(USERNAME, user?.login)
                    put(AVATAR_URL, user?.avatars)
                    put(TYPE, user?.type)
                }
                contentResolver.insert(CONTENT_URI, values)
                sendUpdateFavoriteList(this)
                user?.login
                Toast.makeText(this, "${user?.login} Added to Favorite", Toast.LENGTH_SHORT).show()
                isFavorite = true
                setFabFavorite(isFavorite)
            }
        }

    }

    private fun isFavorited() {
        val dataUserFavorite = contentResolver?.query(uriWithUsername, null, null, null, null)
        val favorite = (1..dataUserFavorite!!.count).map {
            dataUserFavorite.apply {
                moveToNext()
                getInt(dataUserFavorite.getColumnIndexOrThrow(USERNAME))
            }
        }
        if (favorite.isNotEmpty()) {
            isFavorite = true
            setFabFavorite(isFavorite)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSetting -> {
                val mIntent = Intent(this, SettingActivity::class.java)
                startActivity(mIntent)
            }
            R.id.actionFavoriteFromDetail -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return true
    }

    private fun showRecyclerList() {
        detailAdapter = DetailUserAdapter()
        rvDetailActivity.layoutManager = LinearLayoutManager(this)
        rvDetailActivity.adapter = detailAdapter
    }

    private fun setPagerAdapter() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setDetail() {
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
    }

    private fun sendUpdateFavoriteList(context: Context) {
        val mIntent = Intent(context, FavoriteWidget::class.java)
        mIntent.action = FavoriteWidget.UPDATE_WIDGET
        context.sendBroadcast(mIntent)
    }
}
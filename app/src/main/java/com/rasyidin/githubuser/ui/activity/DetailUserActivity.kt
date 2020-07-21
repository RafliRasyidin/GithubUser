package com.rasyidin.githubuser.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import com.rasyidin.githubuser.helper.MappingHelper
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
    private var fromFavorite: String? = null
    private var fromMainActivity: String? = null

    companion object {
        internal val TAG = DetailUserActivity::class.java.simpleName
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_MAIN = "extra_main"
        const val EXTRA_FAVORITE = "extra_favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.title_actionbar_detail)

        showRecyclerList()
        setPagerAdapter()

        fromFavorite = intent.getStringExtra(EXTRA_FAVORITE)
        fromMainActivity = intent.getStringExtra(EXTRA_MAIN)
        user = intent.getParcelableExtra(EXTRA_USERNAME) as User

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.id)

        val dataUserFavorite = contentResolver?.query(uriWithId, null, null, null, null)
        val dataUserObject = MappingHelper.mapCursorToArrayList(dataUserFavorite)
        for (data in dataUserObject) {
            if (this.user?.login == data.login) {
                isFavorite = true
                Log.d(TAG, "This is favorite already")
            }
        }
        setFavorite()

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
        setFabFavorite(isFavorite)
        fabFavorite.setOnClickListener {
            if (isFavorite) {
                user?.let {
                    contentResolver.delete(uriWithId, null, null)
                    Log.d(TAG, "Removed Success")
                    Toast.makeText(this, "${it.login} Removed form Favorite", Toast.LENGTH_SHORT)
                        .show()
                    isFavorite = false
                    setFabFavorite(isFavorite)
                }
            } else {
                val values = ContentValues().apply {
                    put(USERNAME, user?.login)
                    put(AVATAR_URL, user?.avatars)
                    put(TYPE, user?.type)
                }
                contentResolver.insert(CONTENT_URI, values)
                user?.login
                Toast.makeText(this, "${user?.login} Added to Favorite", Toast.LENGTH_SHORT).show()
                isFavorite = true
                setFabFavorite(isFavorite)
            }
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

    override fun onSupportNavigateUp(): Boolean {
        if (fromMainActivity != null) {
            val mIntent = Intent(this, MainActivity::class.java)
            onBackPressed()
            startActivity(mIntent)
        } else if (fromFavorite != null) {
            val mIntent = Intent(this, FavoriteActivity::class.java)
            onBackPressed()
            startActivity(mIntent)
        }
        return super.onSupportNavigateUp()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fromMainActivity != null) {
                val mIntent = Intent(this, MainActivity::class.java)
                startActivity(mIntent)
            } else if (fromFavorite != null) {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onKeyDown(keyCode, event)
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

}
package com.rasyidin.githubuser.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.adapter.FavoriteAdapter
import com.rasyidin.githubuser.database.DatabaseContract.CONTENT_URI
import com.rasyidin.githubuser.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.title = resources.getString(R.string.list_favorites)

        favoriteAdapter = FavoriteAdapter(this)
        loadDataAsync()
        showRecyclerView()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showRecyclerView() {
        rvUserFavorite.adapter = favoriteAdapter
        rvUserFavorite.layoutManager = LinearLayoutManager(this)
        rvUserFavorite.setHasFixedSize(true)
    }

    private fun loadDataAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favorite = deferredUser.await()
            if (favorite.size > 0) {
                favoriteAdapter.listFavorite = favorite
            } else {
                favoriteAdapter.listFavorite = ArrayList()
                Snackbar.make(rvUserFavorite, R.string.data_favorite, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
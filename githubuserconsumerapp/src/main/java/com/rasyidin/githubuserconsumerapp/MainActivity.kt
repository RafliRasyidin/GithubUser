package com.rasyidin.githubuserconsumerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rasyidin.githubuserconsumerapp.adapter.FavoriteAdapter
import com.rasyidin.githubuserconsumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.rasyidin.githubuserconsumerapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = resources.getString(R.string.list_favorites)

        favoriteAdapter = FavoriteAdapter(this)

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
                Snackbar.make(rv_favorite, R.string.empty_favorite, Snackbar.LENGTH_SHORT).show()
            }
        }

        showRecyclerView()
    }

    private fun showRecyclerView() {
        rv_favorite.adapter = favoriteAdapter
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
    }
}

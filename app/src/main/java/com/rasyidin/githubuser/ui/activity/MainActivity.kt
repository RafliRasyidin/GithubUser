package com.rasyidin.githubuser.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.adapter.ListUserAdapter
import com.rasyidin.githubuser.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: ListUserAdapter
    private lateinit var userViewModel: UserViewModel
    private var backPress: Long = 0
    private var backToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showRecyclerList()

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        userViewModel.getUser().observe(this, Observer { userItems ->
            if (userItems != null) {
                userAdapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    showLoading(true)
                    userViewModel.setUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSetting -> {
                val mIntent = Intent(this, SettingActivity::class.java)
                startActivity(mIntent)
            }
            R.id.actionFavorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        backToast = Toast.makeText(baseContext, "Press back again to exit!", Toast.LENGTH_SHORT)
        if (backPress + 2000 > System.currentTimeMillis()) {
            val exit = Intent(Intent.ACTION_MAIN)
            exit.addCategory(Intent.CATEGORY_HOME)
            exit.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(exit)
            backToast?.cancel()
            return super.onKeyDown(keyCode, event)
        } else backToast?.show()

        backPress = System.currentTimeMillis()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarListItem.visibility = View.VISIBLE
        } else {
            progressBarListItem.visibility = View.GONE
            imgSearch.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        userAdapter = ListUserAdapter()
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = userAdapter
    }

}
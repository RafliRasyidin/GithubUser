package com.rasyidin.githubuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rasyidin.githubuser.BuildConfig
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class UserViewModel: ViewModel() {

    companion object {
        private const val TOKEN = "token ${BuildConfig.API_KEY}"
    }
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String) {
        val listItem = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val item = responseObject.getJSONArray("items")

                    for (i in 0 until item.length()) {
                        val user = item.getJSONObject(i)
                        val userItems = User()
                        userItems.login = user.getString("login")
                        userItems.avatars = user.getString("avatar_url")
                        userItems.type = user.getString("type")
                        listItem.add(userItems)

                    }
                    listUser.postValue(listItem)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    fun getUser(): LiveData<ArrayList<User>>{
        return listUser
    }

}
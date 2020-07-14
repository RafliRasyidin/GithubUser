package com.rasyidin.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rasyidin.githubuser.BuildConfig
import com.rasyidin.githubuser.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel: ViewModel() {

    companion object {
        private const val TOKEN = "token ${BuildConfig.API_KEY}"
    }
    val detailUser = MutableLiveData<ArrayList<User>>()

    fun setUserDetail(username: String) {
        val url = "https://api.github.com/users/$username"
        val detailItems = ArrayList<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization",
            TOKEN
        )
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val userItems = User()
                    userItems.apply {
                        login = responseObject.getString("login")
                        name = responseObject.getString("name")
                        location = responseObject.getString("location")
                        company = responseObject.getString("company")
                        followers = responseObject.getInt("followers")
                        following = responseObject.getInt("following")
                        avatars = responseObject.getString("avatar_url")
                    }
                    detailItems.add(userItems)

                    detailUser.postValue(detailItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getDetailUser(): LiveData<ArrayList<User>> {
        return detailUser
    }
}
package com.rasyidin.githubuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel: ViewModel() {

    companion object {
        private const val TOKEN = "token 5d9d5877d8be00e3f525e67793429946f002fcc3"
    }

    val listFollowingUser = MutableLiveData<ArrayList<User>>()

    fun setFollowingUser(username: String) {
        val listItem = ArrayList<User>()

        val url = "https://api.github.com/users/$username/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", TOKEN)
        client.addHeader("User-agent", "request")
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val following = responseArray.getJSONObject(i)
                        val followingItem = User()
                        followingItem.apply {
                            login = following.getString("login")
                            avatars = following.getString("avatar_url")
                            type = following.getString("type")
                        }
                        listItem.add(followingItem)
                    }
                    listFollowingUser.postValue(listItem)

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
                Log.d("onFailure", error!!.message.toString())
            }
        })
    }

    fun getFollowingUser(): LiveData<ArrayList<User>> {
        return listFollowingUser
    }

}
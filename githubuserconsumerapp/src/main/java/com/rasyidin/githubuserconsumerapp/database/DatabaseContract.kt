package com.rasyidin.githubuserconsumerapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.rasyidin.githubuser"
    const val SCHEME = "content"

    class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"
            const val TYPE = "type"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
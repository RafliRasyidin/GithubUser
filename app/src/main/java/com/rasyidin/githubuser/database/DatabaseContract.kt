package com.rasyidin.githubuser.database

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val NAME = "name"
            const val AVATAR_URL = "avatar_url"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val FOLLOWING = "following"
            const val FOLLOWERS = "followers"
        }
    }
}
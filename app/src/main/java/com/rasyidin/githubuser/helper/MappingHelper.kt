package com.rasyidin.githubuser.helper

import android.database.Cursor
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion._ID
import com.rasyidin.githubuser.model.User

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor): ArrayList<User> {
        val favoriteList = ArrayList<User>()

        favoriteCursor.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR_URL))
                val type = getString(getColumnIndexOrThrow(TYPE))
                favoriteList.add(User(id, username, avatar, type))
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(favoriteCursor: Cursor?): User{
        var user = User()
        favoriteCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val username = getString(getColumnIndexOrThrow(USERNAME))
            val avatar = getString(getColumnIndexOrThrow(AVATAR_URL))
            val type = getString(getColumnIndexOrThrow(TYPE))
            user = User(id, username, avatar, type)
        }
        return user
    }
}
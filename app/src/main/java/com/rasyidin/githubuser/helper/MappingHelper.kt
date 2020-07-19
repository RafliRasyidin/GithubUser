package com.rasyidin.githubuser.helper

import android.database.Cursor
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion._ID
import com.rasyidin.githubuser.model.User

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<User> {
        val favoriteList = ArrayList<User>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val user = User()
                user.apply {
                    id = getInt(getColumnIndexOrThrow(_ID))
                    login = getString(getColumnIndexOrThrow(USERNAME))
                    avatars = getString(getColumnIndexOrThrow(AVATAR_URL))
                    type = getString(getColumnIndexOrThrow(TYPE))
                    favoriteList.add(user)
                }
            }
        }
        return favoriteList
    }
}
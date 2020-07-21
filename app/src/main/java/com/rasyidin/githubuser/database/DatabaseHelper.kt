package com.rasyidin.githubuser.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion._ID

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_favorite"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$USERNAME TEXT NOT NULL UNIQUE," +
                "$AVATAR_URL TEXT NOT NULL," +
                "$TYPE TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
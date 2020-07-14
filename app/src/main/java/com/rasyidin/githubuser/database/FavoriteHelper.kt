package com.rasyidin.githubuser.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.rasyidin.githubuser.database.DatabaseContract.FavoriteColumns.Companion._ID

class FavoriteHelper(context: Context) {

    companion object {
        private const val DATABaSE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE: FavoriteHelper? = null
        fun gteInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABaSE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABaSE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABaSE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(TABLE_NAME, "$_ID = '$id'", null)
    }
}
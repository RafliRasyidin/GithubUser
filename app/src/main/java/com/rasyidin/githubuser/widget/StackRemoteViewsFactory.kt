package com.rasyidin.githubuser.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.database.DatabaseContract.CONTENT_URI
import com.rasyidin.githubuser.helper.MappingHelper

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private var listFavorites: Cursor? = null
    private val widgetItem = ArrayList<Bitmap>()

    override fun onCreate() {
        listFavorites = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
    }

    override fun onDataSetChanged() {
        if (listFavorites != null) {
            listFavorites?.close()
        }

        val identityToken = Binder.clearCallingIdentity()

        try {
            listFavorites = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
            val cursor = MappingHelper.mapCursorToArrayList(listFavorites)
            if (cursor.size > 0) {
                widgetItem.clear()
                for (i in 0 until cursor.size) {
                    try {
                        val avatar = cursor[i].avatars.toString()
                        val bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(avatar)
                            .submit()
                            .get()
                        widgetItem.add(bitmap)
                    } catch (e: Exception) {
                        widgetItem.add(
                            BitmapFactory.decodeResource(
                                mContext.resources,
                                R.drawable.baseline_broken_image_black_36
                            )
                        )

                    }

                }
            }
        } catch (e: IllegalStateException) {
            Log.d("ErrorWidget", "${e.message}")
        }
        Binder.restoreCallingIdentity(identityToken)

    }

    override fun getViewAt(position: Int): RemoteViews {
        val remote = RemoteViews(mContext.packageName, R.layout.widget_item)
        remote.setImageViewBitmap(R.id.image_favorite, widgetItem[position])

        val extras = bundleOf(FavoriteWidget.EXTRA_ITEM to position)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        remote.setOnClickFillInIntent(R.id.image_favorite, fillInIntent)
        return remote

    }

    override fun getItemId(position: Int): Long = 0

    override fun getLoadingView(): RemoteViews? = null

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = widgetItem.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() = Unit
}
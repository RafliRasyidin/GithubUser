package com.rasyidin.githubuser.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.rasyidin.githubuser.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}
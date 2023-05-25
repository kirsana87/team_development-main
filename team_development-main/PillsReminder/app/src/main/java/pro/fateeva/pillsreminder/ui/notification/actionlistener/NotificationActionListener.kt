package pro.fateeva.pillsreminder.ui.notification.actionlistener

import android.content.Context
import android.content.Intent
import pro.fateeva.pillsreminder.ui.mainactivity.NotificationHandler

interface NotificationActionListener {
    fun onNotificationAction(notificationHandler: NotificationHandler, context: Context, intent: Intent)
}
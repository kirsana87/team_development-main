package pro.fateeva.pillsreminder.ui.notification.actionlistener

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import pro.fateeva.pillsreminder.ui.mainactivity.NotificationHandler

/**
 * Класс, отвечающий за перехват нажатий на кнопки в уведомлении.
 */
class MedicationActionListener : NotificationActionListener {

    companion object {
        const val MEDICATION_EVENT_INTENT_CATEGORY = "MEDICATION_EVENT_INTENT_CATEGORY"
        const val GET_DRUG_ACTION_EXTRA_KEY = "GET_DRUG_ACTION"
        const val CANCEL_DRUG_ACTION_EXTRA_KEY = "CANCEL_DRUG_ACTION"
        const val NOTIFICATION_ID_EXTRA_KEY = "NOTIFICATION_ID"
        const val NOTIFICATION_REMINDER_TIME_EXTRA_KEY = "REMINDER_TIME"
    }

    override fun onNotificationAction(notificationHandler: NotificationHandler, context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        intent.categories?.let {
            if (intent.categories.contains(MEDICATION_EVENT_INTENT_CATEGORY)) {
                val extras = intent.extras
                if (extras != null) {
                    if (extras.containsKey(GET_DRUG_ACTION_EXTRA_KEY)) {
                        notificationHandler.onGetDrugAction(
                            extras.getInt(NOTIFICATION_ID_EXTRA_KEY),
                            extras.getLong(NOTIFICATION_REMINDER_TIME_EXTRA_KEY),
                            extras.getString(GET_DRUG_ACTION_EXTRA_KEY).toString()
                        )
                    } else if (extras.containsKey(CANCEL_DRUG_ACTION_EXTRA_KEY)) {
                        notificationHandler.onCancelDrugAction(
                            extras.getString(CANCEL_DRUG_ACTION_EXTRA_KEY).toString())
                    }
                    notificationManager.cancel(extras.getInt(NOTIFICATION_ID_EXTRA_KEY))
                }
            }
        }
    }
}
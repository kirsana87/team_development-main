package pro.fateeva.pillsreminder.clean.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.ui.notification.MedicationEventReceiver
import pro.fateeva.pillsreminder.ui.notification.notificationcreator.MedicationNotifier

class NotificationManagerImpl(
    private val context: Context,
) : NotificationManager {

    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    }

    override fun planNotification(
        medicationReminder: MedicationReminder,
        medicationIntakeIndex: Int,
    ) {
        Log.d(
            "NotificationManager",
            "Planning notification for ${medicationReminder.medicationIntakes[medicationIntakeIndex].time}"
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            medicationReminder.medicationIntakes[medicationIntakeIndex].time,
            getPendingIntent(medicationReminder, medicationIntakeIndex)
        )
    }

    private fun getPendingIntent(
        medicationReminder: MedicationReminder,
        medicationIntakeIndex: Int,
    ): PendingIntent {
        val medicationReminderIntent = Intent(context, MedicationEventReceiver::class.java).apply {

            putExtra(
                MedicationNotifier.NOTIFICATION_TITLE_EXTRA_KEY,
                context.getString(R.string.its_time_to_medication)
            )

            putExtra(
                MedicationNotifier.NOTIFICATION_DRUG_NAME_EXTRA_KEY,
                medicationReminder.medicationName
            )

            putExtra(
                MedicationNotifier.NOTIFICATION_DOSAGE_EXTRA_KEY,
                medicationReminder.medicationIntakes[medicationIntakeIndex].dosage
            )

            putExtra(
                MedicationNotifier.NOTIFICATION_ID_EXTRA_KEY,
                medicationReminder.id
            )

            putExtra(
                MedicationNotifier.REMINDER_TIME_EXTRA_KEY,
                medicationReminder.medicationIntakes[medicationIntakeIndex].time
            )
        }

        return PendingIntent.getBroadcast(
            context,
            medicationReminder.id,
            medicationReminderIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun deleteNotification(medicationReminder: MedicationReminder) {
        alarmManager.cancel(getPendingIntent(medicationReminder, 0)) //Index does not matter
    }
}
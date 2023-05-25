package pro.fateeva.pillsreminder.ui.notification.notificationcreator

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.ui.mainactivity.MainActivity
import pro.fateeva.pillsreminder.ui.notification.actionlistener.MedicationActionListener

private const val DEFAULT_REQUEST_CODE = -1
private const val DEFAULT_REMINDER_TIME = -1L
private const val CHANNEL_ID = "Прием лекарств"

class MedicationNotifier : MedicationNotification {

    companion object {
        const val NOTIFICATION_TITLE_EXTRA_KEY = "NOTIFICATION_TITLE"
        const val NOTIFICATION_DRUG_NAME_EXTRA_KEY = "NOTIFICATION_DRUG_NAME"
        const val NOTIFICATION_DOSAGE_EXTRA_KEY = "NOTIFICATION_DOSAGE"
        const val NOTIFICATION_ID_EXTRA_KEY = "NOTIFICATION_REQUEST_CODE"
        const val REMINDER_TIME_EXTRA_KEY = "REMINDER_TIME_EXTRA_KEY"
    }

    override fun showNotification(context: Context, intent: Intent) {
        val notificationTitle =
            (intent.extras?.getString(NOTIFICATION_TITLE_EXTRA_KEY))
                ?: context.getString(R.string.notification_title_error)

        val drugName = (intent.extras?.getString(NOTIFICATION_DRUG_NAME_EXTRA_KEY))
            ?: context.getString(R.string.drug_name_error)

        val dosage = (intent.extras?.getInt(NOTIFICATION_DOSAGE_EXTRA_KEY))
            ?: context.getString(R.string.dosage_error)

        val requestCode = intent.extras?.getInt(NOTIFICATION_ID_EXTRA_KEY) ?: DEFAULT_REQUEST_CODE

        val reminderTime = intent.extras?.getLong(REMINDER_TIME_EXTRA_KEY) ?: DEFAULT_REMINDER_TIME

        val onGetDrugActionIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(MedicationActionListener.MEDICATION_EVENT_INTENT_CATEGORY)
            putExtra(MedicationActionListener.NOTIFICATION_ID_EXTRA_KEY, requestCode)
            putExtra(MedicationActionListener.NOTIFICATION_REMINDER_TIME_EXTRA_KEY, reminderTime)
        }

        val onCancelDrugActionIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(MedicationActionListener.MEDICATION_EVENT_INTENT_CATEGORY)
            putExtra(MedicationActionListener.NOTIFICATION_ID_EXTRA_KEY, requestCode)
        }

        val onGetDrugPendingIntent =
            PendingIntent.getActivity(
                context,
                requestCode,
                onGetDrugActionIntent.putExtra(MedicationActionListener.GET_DRUG_ACTION_EXTRA_KEY,
                    context.getString(R.string.confirmed_medication_action)),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val onCancelDrugPendingIntent =
            PendingIntent.getActivity(
                context,
                -requestCode, // requestCode со знаком "-", чтобы в системе могло быть 2 уникальных PendingIntent
                onCancelDrugActionIntent.putExtra(MedicationActionListener.CANCEL_DRUG_ACTION_EXTRA_KEY,
                    context.getString(R.string.canceled_medication_action)),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_medical_services)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setContentTitle(notificationTitle)
                .setStyle(NotificationCompat.InboxStyle()
                    .addLine(context.getString(R.string.notification_drug_name_title, drugName))
                    .addLine(context.getString(R.string.notification_dosage_title, dosage)))
                .addAction(R.drawable.ic_accept_medication,
                    context.getString(R.string.get_drug_notification_button),
                    onGetDrugPendingIntent)
                .addAction(R.drawable.ic_cancel_medication,
                    context.getString(R.string.cancel_drug_notification_button),
                    onCancelDrugPendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH))
        }

        notificationManager.notify(requestCode, notificationBuilder.build())
    }
}


package pro.fateeva.pillsreminder.ui.notification.notificationcreator

import android.content.Context
import android.content.Intent

/**
 * Класс, отвечающий за формирование и показ уведомлений
 */
interface MedicationNotification {
    fun showNotification(context: Context, intent: Intent)
}
package pro.fateeva.pillsreminder.ui.mainactivity

import android.app.AlarmManager
import pro.fateeva.pillsreminder.ui.notification.actionlistener.NotificationActionListener

/**
 * Контракт для MainActivity как сущности, которая генерирут уведомления
 */
interface NotificationHandler {
    val alarmManager: AlarmManager
    val actionListener: NotificationActionListener
    fun onGetDrugAction(pillID: Int, plannedMedicationTime: Long, message: String)
    fun onCancelDrugAction(message: String)
}
package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder

interface NotificationManager {
    fun planNotification(medicationReminder: MedicationReminder, medicationIntakeIndex: Int)
    fun deleteNotification(medicationReminder: MedicationReminder)
}
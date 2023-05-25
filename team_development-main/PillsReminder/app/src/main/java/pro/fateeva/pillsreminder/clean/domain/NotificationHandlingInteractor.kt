package pro.fateeva.pillsreminder.clean.domain

import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepository

class NotificationHandlingInteractor(private val repository: MedicationReminderRepository) {
    fun setActualMedicationTime(pillID: Int, medicationTime: Long, actualMedicationTime: Long) {
        repository.updateMedicationIntake(pillID, medicationTime, actualMedicationTime)
    }
}
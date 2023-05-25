package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

interface MedicationReminderRepository {
    fun saveMedicationReminder(medicationReminder: MedicationReminder)
    fun getMedicationReminder(id: Int) : MedicationReminder
    fun getMedicationReminders(): List<MedicationReminder>
    fun deleteMedicationReminder(id: Int)
    fun getCalendarData(): List<MedicationScheduleItemDomain>
    fun updateMedicationIntake(pillID: Int, medicationTime: Long, actualMedicationTime: Long)
}
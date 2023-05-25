package pro.fateeva.pillsreminder.clean.domain

import android.util.Log
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepository
import pro.fateeva.pillsreminder.clean.data.NotificationManager
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.extensions.copyDateFrom
import pro.fateeva.pillsreminder.extensions.toCalendar
import pro.fateeva.pillsreminder.extensions.toCalendarDateOnly
import java.util.*

class MedicationInteractor(
    private val notificationManager: NotificationManager,
    private val medicationReminderRepository: MedicationReminderRepository
) {
    fun saveMedicationReminder(quantityOfDays: Int, medicationReminder: MedicationReminder) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, quantityOfDays)
        medicationReminder.endDate = calendar.timeInMillis

        updateRemindersTime(medicationReminder)

        notificationManager.planNotification(
            medicationReminder,
            0
        )

        medicationReminderRepository.saveMedicationReminder(medicationReminder)
    }

    fun editMedicationReminder(medicationReminder: MedicationReminder) {
        updateRemindersTime(medicationReminder)

        notificationManager.planNotification(
            medicationReminder,
            0
        )

        medicationReminderRepository.saveMedicationReminder(medicationReminder)
    }

    fun planMedicationReminders(){
        medicationReminderRepository.getMedicationReminders().forEach {
            planReminder(it)
        }
    }

    private fun planReminder(medicationReminder: MedicationReminder){
        updateRemindersTime(medicationReminder)
        val now = System.currentTimeMillis()
        val nextIndex = medicationReminder.medicationIntakes.indexOfFirst { it.time >= now }

        if (nextIndex == -1) error("Next reminder not found")

        val nextMedicationReminderTime = medicationReminder.medicationIntakes[nextIndex].time
        Log.d(TAG, "Next reminder: ${nextMedicationReminderTime.toCalendar().time}")
        Log.d(TAG, "End date reminder: ${medicationReminder.endDate.toCalendarDateOnly().time}")

        if (nextMedicationReminderTime.toCalendarDateOnly().timeInMillis < medicationReminder.endDate.toCalendarDateOnly().timeInMillis) {
            notificationManager.planNotification(medicationReminder, nextIndex)
        }
    }

    fun onNotificationShown(medicationReminder: MedicationReminder, previousReminderTime: Long) {
        Log.d(TAG, "onNotificationShown. Prev reminder time ${previousReminderTime.toCalendar().time}")
        planReminder(medicationReminder)
    }

    fun getMedicationReminders() : List<MedicationReminder>{
        return medicationReminderRepository.getMedicationReminders()
    }

    fun getMedicationReminder(id: Int) : MedicationReminder{
        return medicationReminderRepository.getMedicationReminder(id)
    }

    private fun updateRemindersTime(medicationReminder: MedicationReminder) {
        medicationReminder.medicationIntakes = medicationReminder.medicationIntakes.map {
            it.copy(time = addDayToMedicationReminder(it.time))
        }.sortedBy { it.time }
    }

    private fun addDayToMedicationReminder(time: Long): Long {
        val calendarReminder = time.toCalendar()
        if (time < System.currentTimeMillis()) {
            calendarReminder.copyDateFrom(Calendar.getInstance())
            calendarReminder.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendarReminder.timeInMillis
    }

    fun deleteMedicationReminder(id: Int){
        notificationManager.deleteNotification(getMedicationReminder(id))
        medicationReminderRepository.deleteMedicationReminder(id)
    }

    companion object {
        const val TAG = "MedicationReminder"
    }
}
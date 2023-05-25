package pro.fateeva.pillsreminder.clean.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationReminderEntity

@Dao
interface MedicationReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMedicationReminder(medicationReminder: MedicationReminderEntity)

    @Query("SELECT * FROM MedicationReminderEntity")
    fun getAllMedicationReminders(): List<MedicationReminderEntity>

    @Query("SELECT * FROM MedicationReminderEntity WHERE pillID = :pillID")
    fun getMedicationReminder(pillID: Int): MedicationReminderEntity

    @Query("DELETE FROM MedicationReminderEntity WHERE pillID = :pillID")
    fun deleteMedicationReminder(pillID: Int)
}
package pro.fateeva.pillsreminder.clean.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationIntakeEntity

@Dao
interface MedicationIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMedicationIntake(medicationIntake: MedicationIntakeEntity)

    @Query("SELECT * FROM MedicationIntakeEntity")
    fun getAllMedicationIntakes(): List<MedicationIntakeEntity>

    @Query("SELECT * FROM MedicationIntakeEntity WHERE intakeID = :intakeID ORDER BY medicationTime ASC LIMIT 2")
    fun getMedicationIntakes(intakeID: Int): List<MedicationIntakeEntity>

    @Query("SELECT * FROM MedicationIntakeEntity WHERE intakeID = :intakeID ORDER BY medicationTime ASC LIMIT :limit")
    fun getMedicationIntakesByIndicesLimit(intakeID: Int, limit: Int): List<MedicationIntakeEntity>

    @Query("SELECT DISTINCT intakeIndex FROM MedicationIntakeEntity WHERE intakeID = :pillID")
    fun getMedicationIntakeIndices(pillID: Int): List<Int>

    @Query("DELETE FROM MedicationIntakeEntity WHERE intakeID = :pillID")
    fun deleteMedicationIntake(pillID: Int)

    @Query("DELETE FROM MedicationIntakeEntity WHERE intakeID == :id AND medicationTime > :currentTime")
    fun deletePlannedIntakes(id: Int, currentTime: Long)

    @Query("UPDATE MedicationIntakeEntity SET actualMedicationTime = :actualMedicationTime WHERE intakeID = :pillID AND medicationTime = :medicationTime")
    fun updateMedicationIntake(pillID: Int, medicationTime: Long, actualMedicationTime: Long)
}
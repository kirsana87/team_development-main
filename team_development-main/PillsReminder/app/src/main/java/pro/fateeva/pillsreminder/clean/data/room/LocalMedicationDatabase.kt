package pro.fateeva.pillsreminder.clean.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationIntakeEntity
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationReminderEntity

private const val DB_NAME = "LocalMedicationDB"

@Database(
    entities = [MedicationReminderEntity::class, MedicationIntakeEntity::class],
    version = 1,
    exportSchema = true)
abstract class LocalMedicationDatabase: RoomDatabase() {
    abstract val medicationReminderDao: MedicationReminderDao
    abstract val medicationIntakeDao: MedicationIntakeDao

    companion object {
        private var INSTANCE: LocalMedicationDatabase? = null

        fun getUserDatabase(context: Context): LocalMedicationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    LocalMedicationDatabase::class.java,
                    DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
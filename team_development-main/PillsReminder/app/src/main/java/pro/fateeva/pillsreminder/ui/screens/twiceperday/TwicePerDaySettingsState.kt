package pro.fateeva.pillsreminder.ui.screens.twiceperday

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class TwicePerDaySettingsState(
    var medicationName: String = "",
    var firstMedicationReminderTime: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis,
    var firstMedicationDose: Int = 1,
    var secondMedicationReminderTime: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 20)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis,
    var secondMedicationDose: Int = 1
) : Parcelable

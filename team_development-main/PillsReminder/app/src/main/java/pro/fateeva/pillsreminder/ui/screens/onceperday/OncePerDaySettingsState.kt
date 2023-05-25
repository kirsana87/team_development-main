package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class OncePerDaySettingsState(
    var medicationName: String = "",
    var medicationReminderTime: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis,
    var medicationDose: Int = 1
) : Parcelable

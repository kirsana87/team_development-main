package pro.fateeva.pillsreminder.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.toCalendar(): Calendar = Calendar.getInstance().apply { timeInMillis = this@toCalendar }

fun Calendar.copyTimeFrom(other: Calendar) {
    set(Calendar.HOUR_OF_DAY, other.get(Calendar.HOUR_OF_DAY))
    set(Calendar.MINUTE, other.get(Calendar.MINUTE))
    set(Calendar.SECOND, other.get(Calendar.SECOND))
    set(Calendar.MILLISECOND, other.get(Calendar.MILLISECOND))
}

fun Calendar.copyDateFrom(other: Calendar) {
    set(Calendar.DAY_OF_MONTH, other.get(Calendar.DAY_OF_MONTH))
    set(Calendar.MONTH, other.get(Calendar.MONTH))
    set(Calendar.YEAR, other.get(Calendar.YEAR))
}

fun Long.toCalendarDateOnly(): Calendar {
    val calendar = toCalendar()
    calendar.set(Calendar.HOUR, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar
}

fun Calendar.formatTime() : String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)
}
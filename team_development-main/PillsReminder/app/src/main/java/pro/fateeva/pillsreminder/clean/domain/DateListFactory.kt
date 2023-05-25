package pro.fateeva.pillsreminder.clean.domain

import java.util.*

class DateListFactory {
    fun getDatesList(datesCount: Int): List<Long> {
        val dateList = mutableListOf<Long>()
        for (i in -datesCount / 2..(-datesCount / 2) + datesCount) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, i)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            dateList.add(calendar.timeInMillis)
        }
        return dateList
    }
}
package pro.fateeva.pillsreminder.extensions

import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import pro.fateeva.pillsreminder.R
import java.util.*

private const val DEFAULT_TIME_PICKER_VALUE = 0
private const val TOMORROW_DATE_OFFSET = 1
private const val TIME_PICKER_TAG = "TIME_PICKER"

fun View.initTimePicker(
    time: Long,
    fragmentManager: FragmentManager,
    tag: String? = TIME_PICKER_TAG,
    onTimeSet: (Calendar) -> Unit,
) {
    val calendar = time.toCalendar()

    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(if (android.text.format.DateFormat.is24HourFormat(this.context)) {
            TimeFormat.CLOCK_24H
        } else {
            TimeFormat.CLOCK_12H
        })
        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
        .setMinute(calendar.get(Calendar.MINUTE))
        .setTitleText(context.getString(R.string.time_picker_title))
        .build()

    timePicker.addOnPositiveButtonClickListener {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePicker.hour)
            set(Calendar.MINUTE, timePicker.minute)
            set(Calendar.SECOND, DEFAULT_TIME_PICKER_VALUE)
            set(Calendar.MILLISECOND, DEFAULT_TIME_PICKER_VALUE)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DATE, TOMORROW_DATE_OFFSET)
            }
        }
        onTimeSet(calendar)
    }

    setOnClickListener {
        timePicker.show(fragmentManager, tag)
    }
}
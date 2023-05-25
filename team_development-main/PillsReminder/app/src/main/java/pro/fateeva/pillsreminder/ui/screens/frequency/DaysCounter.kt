package pro.fateeva.pillsreminder.ui.screens.frequency

import android.annotation.SuppressLint
import android.widget.TextView

private const val MIN_DAYS_COUNT_VALUE = "1"

class DaysCounter {
    fun decrement(textView: TextView) {
        textView.text =
            if (textView.text.toString().toInt() > MIN_DAYS_COUNT_VALUE.toInt()
            ) {
                (textView.text.toString().toInt() - 1).toString()
            } else {
                MIN_DAYS_COUNT_VALUE
            }
    }

    @SuppressLint("SetTextI18n")
    fun increment(textView: TextView) {
        textView.text = (textView.text.toString().toInt() + 1).toString()
    }
}
package pro.fateeva.pillsreminder.ui.screens.calendar.builder

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.DateListFactory
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain
import pro.fateeva.pillsreminder.databinding.ItemScheduleCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Класс, отвечающий за построение календарной сетки.
 * Календарная сетка настраивается основными параметрами:
 * @DATES_COUNT - количество отображаемых дат в календаре (даты рассчитываются по принципу:
 * половина DATES_COUNT - до текущей системной даты (для просмотра прошедших в эти даты
 * событий), половина - после (для просмотра запланированных событий). Если выставить значение
 * меньше 7, то для корректного отображения необходимо выставить такое же значение
 * COLUMNS_COUNT;
 * @IS_OFFSET_REQUIRED - если true, то календарнная сетка сдвинется в зависимости от текущего
 * дня недели (из расчета, что первый день недели - понедельник). Если false - календарная сетка не
 * будет учитывать дни недели (при выставлении количества дат кратно 7 на экране календарная сетка
 * отобразится в виде равномерно заполненного прямоугольнака);
 * @COLUMNS_COUNT - количество ячеек в одной строке календарной сетки
 */

private const val DATES_COUNT = 30 // не рекомендуется выставлять меньше 7 (т.к. 7 дней недели)
private const val COLUMNS_COUNT = 7 // не менять, если IS_OFFSET_REQUIRED = true
private const val IS_OFFSET_REQUIRED = true // если DATES_COUNT < 7, то выставить false

private const val DEFAULT_LAYOUT_SIZE = 0
private const val LAYOUT_DIMENSION_RATIO = "W,1:1"
private const val DATE_DELIMITER = '.'

class CalendarGridBuilder(private val dateFormat: SimpleDateFormat) {

    private val dateList = DateListFactory().getDatesList(DATES_COUNT)

    private val todayDate = dateFormat.format(Calendar.getInstance().time)

    private val firstDateInList = Calendar.getInstance().apply {
        timeInMillis = dateList[0]
    }

    private lateinit var scheduleCalendarItemView: View

    private var medicationEventList = listOf<MedicationScheduleItemDomain>()

    private val offset = initOffset()

    fun buildCalendarGrid(
        calendarContainer: ConstraintLayout,
        calendarHeader: ConstraintLayout,
        medicationEventList: List<MedicationScheduleItemDomain>,
        onBuildEndAction: () -> Unit,
        block: (currentDate: String) -> Unit,
    ) {
        calendarContainer.removeAllViews()

        val context = calendarContainer.context

        this.medicationEventList = medicationEventList

        calendarHeader.isVisible = IS_OFFSET_REQUIRED

        repeat(DATES_COUNT + offset) { index ->

            val scheduleCalendarItemBinding = ItemScheduleCalendarBinding
                .inflate(LayoutInflater.from(context))

            val params = ConstraintLayout.LayoutParams(DEFAULT_LAYOUT_SIZE, DEFAULT_LAYOUT_SIZE)
            params.dimensionRatio = LAYOUT_DIMENSION_RATIO

            initScheduleCalendarItemView(
                binding = scheduleCalendarItemBinding,
                calendarContainer = calendarContainer,
                index = index,
                params = params,
                block = block)

            setParams(scheduleCalendarItemView, params, index)

            calendarContainer.addView(scheduleCalendarItemView)
        }
        onBuildEndAction.invoke()
    }

    private fun initOffset(): Int {
        return if (IS_OFFSET_REQUIRED) {
            if (firstDateInList.get(Calendar.DAY_OF_WEEK) == 1) {
                firstDateInList.get(Calendar.DAY_OF_WEEK) + 5
            } else {
                firstDateInList.get(Calendar.DAY_OF_WEEK) - 2
            }
        } else {
            0
        }
    }

    private fun initScheduleCalendarItemView(
        binding: ItemScheduleCalendarBinding,
        calendarContainer: ConstraintLayout,
        index: Int,
        params: ConstraintLayout.LayoutParams,
        block: (currentDate: String) -> Unit,
    ) {
        with(binding) {
            when (index) {
                in 0 until offset -> {
                    scheduleCalendarItemView = View(binding.root.context)
                }
                else -> {
                    val currentDate = dateFormat.format(dateList[index - offset])

                    scheduleCalendarItemView = binding.root.apply {
                        initCalendarItemCard(calendarContainer,
                            scheduleCalendarItemCard, currentDate, block)
                    }

                    setCalendarItemText(scheduleCalendarItemCardMmYyTextView,
                        scheduleCalendarItemCardDayTextView, currentDate)

                    initScheduleItemMarker(scheduleCalendarItemCardEventMarkerView,
                        medicationEventList, currentDate)

                    if (dateFormat.parse(currentDate) == dateFormat.parse(todayDate)) {
                        scheduleCalendarItemCard.setCardBackgroundColor(
                            binding.root.context.getColor(R.color.selected_date))

                        block.invoke(currentDate)
                    }
                }
            }
        }

        scheduleCalendarItemView.apply {
            id = index + 1
            layoutParams = params
        }
    }

    private fun initCalendarItemCard(
        calendarContainer: ConstraintLayout,
        itemCardView: MaterialCardView,
        currentDate: String,
        block: (currentDate: String) -> Unit,
    ) {
        itemCardView.setOnClickListener {
            with(itemCardView.context) {
                calendarContainer.children.forEach { childrenView ->
                    if (childrenView.id !in 1..offset) {
                        childrenView.findViewById<MaterialCardView>(R.id.schedule_calendar_item_card)
                            .setCardBackgroundColor(getColor(R.color.calendar_item))
                    }
                }
                itemCardView.setCardBackgroundColor(getColor(R.color.selected_date))
            }

            block.invoke(currentDate)
        }
    }

    private fun initScheduleItemMarker(
        markerView: View,
        medicationEventList: List<MedicationScheduleItemDomain>,
        currentDate: String,
    ) {
        dateFormat.parse(currentDate)?.let { date ->
            var isMedicationSuccess = true
            medicationEventList
                .filter { dateFormat.format(it.medicationTime) == currentDate }
                .forEach {
                    markerView.isVisible = true
                    if (date.before(dateFormat.parse(todayDate))) {
                        if (it.actualMedicationTime == -1L) {
                            isMedicationSuccess = false
                        }
                        setCalendarItemMarkerColor(markerView, isMedicationSuccess)
                    }
                }
        }
    }

    private fun setCalendarItemMarkerColor(markerView: View, isMedicationSuccess: Boolean) {
        markerView.apply {
            setBackgroundColor(context.getColor(if (isMedicationSuccess) {
                R.color.success_medication
            } else {
                R.color.failure_medication
            }))
        }
    }

    private fun setCalendarItemText(date: TextView, day: TextView, currentDate: String) {
        date.text = currentDate.substringAfter(DATE_DELIMITER)
        day.text = currentDate.substringBefore(DATE_DELIMITER)
    }

    private fun setParams(view: View, params: ConstraintLayout.LayoutParams, index: Int) {
        when (index) {
            0 -> {
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                params.endToStart = view.id + 1
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }
            in 1 until COLUMNS_COUNT - 1 -> {
                params.startToEnd = view.id - 1
                params.endToStart = view.id + 1
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }
            COLUMNS_COUNT - 1 -> {
                params.startToEnd = view.id - 1
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }
            else -> {
                params.startToStart = view.id - COLUMNS_COUNT
                params.endToEnd = view.id - COLUMNS_COUNT
                params.topToBottom = view.id - COLUMNS_COUNT
            }
        }
    }
}
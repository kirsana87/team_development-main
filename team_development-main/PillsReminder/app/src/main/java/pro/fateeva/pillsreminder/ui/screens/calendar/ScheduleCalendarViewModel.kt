package pro.fateeva.pillsreminder.ui.screens.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepository
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

class ScheduleCalendarViewModel(
    private val repository: MedicationReminderRepository,
) : ViewModel() {
    private val liveData = MutableLiveData<List<MedicationScheduleItemDomain>>()

    fun getMedicationScheduleList(): LiveData<List<MedicationScheduleItemDomain>> {
        liveData.postValue(repository.getCalendarData())
        return liveData
    }
}
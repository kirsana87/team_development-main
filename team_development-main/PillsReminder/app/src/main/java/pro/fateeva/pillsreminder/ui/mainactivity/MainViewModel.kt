package pro.fateeva.pillsreminder.ui.mainactivity

import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.domain.NotificationHandlingInteractor

class MainViewModel(private val interactor: NotificationHandlingInteractor): ViewModel() {
    fun setActualMedicationTime(pillID: Int, medicationTime: Long, actualMedicationTime: Long) {
        interactor.setActualMedicationTime(pillID, medicationTime, actualMedicationTime)
    }
}
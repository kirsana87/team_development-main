package pro.fateeva.pillsreminder.ui.screens.twiceperday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.domain.MedicationInteractor
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationIntake
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.ui.OperationState

private const val FIRST_MEDICATION_INTAKE_INDEX = 0
private const val SECOND_MEDICATION_INTAKE_INDEX = 1

class TwicePerDaySettingsViewModel(
    private val handle: SavedStateHandle,
    private val interactor: MedicationInteractor
) : ViewModel() {

    private val liveData: MutableLiveData<TwicePerDaySettingsState> =
        handle.getLiveData("state", TwicePerDaySettingsState())
    private val firstMedicationDoseError = MutableLiveData(false)
    private val secondMedicationDoseError = MutableLiveData(false)

    private val twicePerDaySettingsState: TwicePerDaySettingsState
        get() = liveData.value ?: TwicePerDaySettingsState()

    val state: LiveData<TwicePerDaySettingsState>
        get() = liveData

    val hasFirstMedicationDoseError: LiveData<Boolean>
        get() = firstMedicationDoseError

    val hasSecondMedicationDoseError: LiveData<Boolean>
        get() = secondMedicationDoseError

    private fun isEveryFieldValid() = twicePerDaySettingsState.let {
        it.firstMedicationDose > 0 && it.firstMedicationReminderTime > 0 && it.secondMedicationDose > 0 && it.secondMedicationReminderTime > 0
    }

    private val successErrorSaveLiveData: MutableLiveData<OperationState> =
        handle.getLiveData("saveState")

    val successErrorSaveState: LiveData<OperationState>
        get() = successErrorSaveLiveData

    private val deleteStateLiveData: MutableLiveData<OperationState> =
        handle.getLiveData("saveState")
    val deleteState: LiveData<OperationState>
        get() = deleteStateLiveData

    private val canDeleteLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val canDelete: LiveData<Boolean>
        get() = canDeleteLiveData

    fun setMedicationReminderTime(time: Long, medicationIntakeIndex: Int) {
        if (medicationIntakeIndex == FIRST_MEDICATION_INTAKE_INDEX) {
            twicePerDaySettingsState.firstMedicationReminderTime = time
        } else if (medicationIntakeIndex == SECOND_MEDICATION_INTAKE_INDEX) {
            twicePerDaySettingsState.secondMedicationReminderTime = time
        }
    }

    fun setDose(dose: String, medicationIntakeIndex: Int) {
        val isError = dose == "" || dose.toInt() == 0
        if (medicationIntakeIndex == FIRST_MEDICATION_INTAKE_INDEX) {
            twicePerDaySettingsState.firstMedicationDose = dose.toIntOrNull() ?: 0
            firstMedicationDoseError.value = isError
        } else if (medicationIntakeIndex == SECOND_MEDICATION_INTAKE_INDEX) {
            twicePerDaySettingsState.secondMedicationDose = dose.toIntOrNull() ?: 0
            secondMedicationDoseError.value = isError
        }
    }

    fun onCreateMedicationReminder(quantityOfDays: Int, selectedDrug: DrugDomain) {
        val medicationReminder = MedicationReminder(
            selectedDrug.ID,
            selectedDrug.drugName,
            listOf(
                MedicationIntake(
                    twicePerDaySettingsState.firstMedicationDose,
                    twicePerDaySettingsState.firstMedicationReminderTime
                ),
                MedicationIntake(
                    twicePerDaySettingsState.secondMedicationDose,
                    twicePerDaySettingsState.secondMedicationReminderTime
                )
            )
        )

        if (twicePerDaySettingsState.firstMedicationDose == 0 || twicePerDaySettingsState.firstMedicationDose.toString() == "") {
            firstMedicationDoseError.value = true
        }

        if (twicePerDaySettingsState.secondMedicationDose == 0 || twicePerDaySettingsState.secondMedicationDose.toString() == "") {
            secondMedicationDoseError.value = true
        }

        if (isEveryFieldValid()) {
            interactor.saveMedicationReminder(quantityOfDays, medicationReminder)
            successErrorSaveLiveData.value = OperationState.SUCCESS
        }
    }

    fun onEditMedicationReminder(id: Int) {
        val medicationReminder = interactor.getMedicationReminder(id)
        val newMedicationReminder = MedicationReminder(
            medicationReminder.id,
            medicationReminder.medicationName,
            listOf(
                MedicationIntake(
                    twicePerDaySettingsState.firstMedicationDose,
                    twicePerDaySettingsState.firstMedicationReminderTime
                ),
                MedicationIntake(
                    twicePerDaySettingsState.secondMedicationDose,
                    twicePerDaySettingsState.secondMedicationReminderTime
                )
            ),
            medicationReminder.endDate
        )

        if (twicePerDaySettingsState.firstMedicationDose == 0 || twicePerDaySettingsState.firstMedicationDose.toString() == "") {
            firstMedicationDoseError.value = true
        }

        if (twicePerDaySettingsState.secondMedicationDose == 0 || twicePerDaySettingsState.secondMedicationDose.toString() == "") {
            secondMedicationDoseError.value = true
        }

        if (isEveryFieldValid()) {
            interactor.editMedicationReminder(newMedicationReminder)
            successErrorSaveLiveData.value = OperationState.SUCCESS
        }
    }

    fun onViewCreated(medicationReminderId: Int) {
        canDeleteLiveData.value = true
        val medicationReminder = interactor.getMedicationReminder(medicationReminderId)
        setMedicationReminderTime(medicationReminder.medicationIntakes[0].time, 0)
        setMedicationReminderTime(medicationReminder.medicationIntakes[1].time, 1)
        setDose(medicationReminder.medicationIntakes[0].dosage.toString(), 0)
        setDose(medicationReminder.medicationIntakes[1].dosage.toString(), 1)
        twicePerDaySettingsState.medicationName = medicationReminder.medicationName
    }

    fun onViewCreated(name: String) {
        twicePerDaySettingsState.medicationName = name
    }

    fun deleteReminder(id: Int) {
        interactor.deleteMedicationReminder(id)
        deleteStateLiveData.value = OperationState.SUCCESS
    }
}
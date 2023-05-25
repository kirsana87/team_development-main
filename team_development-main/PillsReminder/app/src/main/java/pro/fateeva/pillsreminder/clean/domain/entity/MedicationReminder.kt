package pro.fateeva.pillsreminder.clean.domain.entity

data class MedicationReminder(
    val id: Int,
    val medicationName: String,
    var medicationIntakes: List<MedicationIntake>,
    var endDate: Long = -1
)
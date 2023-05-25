package pro.fateeva.pillsreminder.clean.domain.entity

data class MedicationScheduleItemDomain(
    val pillId: Int,
    val pillName: String,
    val medicationTime: Long,
    val actualMedicationTime: Long,
)
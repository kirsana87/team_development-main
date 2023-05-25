package pro.fateeva.pillsreminder.clean.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Поля класса, возможно, будуд скорректированы в зависимости от роста функционала приложения
 */
@Parcelize
data class DrugDomain(
    val ID: Int = -1,
    val drugName: String = "Обычное лекарство",
): Parcelable

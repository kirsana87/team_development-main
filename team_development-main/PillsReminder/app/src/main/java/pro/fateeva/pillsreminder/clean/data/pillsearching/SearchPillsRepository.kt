package pro.fateeva.pillsreminder.clean.data.pillsearching

import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain

interface SearchPillsRepository {
    suspend fun searchPills(query: String): List<DrugDomain>
}
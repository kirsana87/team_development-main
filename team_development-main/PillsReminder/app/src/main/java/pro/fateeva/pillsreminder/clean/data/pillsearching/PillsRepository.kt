package pro.fateeva.pillsreminder.clean.data.pillsearching

import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain

private const val PILL_TYPE_IDENTIFIER = "tn"

class PillsRepository(private val retrofit: PillsApi) : SearchPillsRepository {

    override suspend fun searchPills(query: String): List<DrugDomain> {
        return retrofit.searchPillsAsync(query)
            .await()
            .filter { it.type == PILL_TYPE_IDENTIFIER }
            .map { DrugDomain(it.id, it.name) }
    }
}
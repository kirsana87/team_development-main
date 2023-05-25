package pro.fateeva.pillsreminder.ui.screens.pillsearching

import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain

sealed class SearchPillState {
    object Loading: SearchPillState()
    class Success(val dataList: List<DrugDomain>): SearchPillState()
    class Error(val error: String): SearchPillState()
}
package pro.fateeva.pillsreminder.ui.screens.pillsearching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pro.fateeva.pillsreminder.clean.data.pillsearching.SearchPillsRepository

class SearchPillViewModel(private val searchPillRepository: SearchPillsRepository) : ViewModel() {

    private val liveData = MutableLiveData<SearchPillState>()
    private val searchingScope = viewModelScope

    fun getData(): LiveData<SearchPillState> = liveData

    suspend fun searchPills(query: String) {
        if (query.isNotEmpty()) {
            liveData.postValue(SearchPillState.Loading)
            searchingScope.launch {
                try {
                    liveData.postValue(
                        SearchPillState.Success(searchPillRepository.searchPills(query)))
                } catch (e: Exception) {
                    liveData.postValue(SearchPillState.Error(e.message.toString()))
                }
            }
        } else {
            liveData.postValue(SearchPillState.Success(listOf()))
        }
    }
}
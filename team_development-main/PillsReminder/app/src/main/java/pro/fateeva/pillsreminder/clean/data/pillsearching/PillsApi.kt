package pro.fateeva.pillsreminder.clean.data.pillsearching

import kotlinx.coroutines.Deferred
import pro.fateeva.pillsreminder.clean.data.pillsearching.dto.PillSearchResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PillsApi {
    @GET("rlsnet/search/global_search")
    fun searchPillsAsync(@Query("word") query: String) : Deferred<List<PillSearchResultDto>>
}
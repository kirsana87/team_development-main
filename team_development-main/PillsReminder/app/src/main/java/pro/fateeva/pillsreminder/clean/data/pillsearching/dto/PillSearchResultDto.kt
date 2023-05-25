package pro.fateeva.pillsreminder.clean.data.pillsearching.dto

import com.google.gson.annotations.SerializedName

data class PillSearchResultDto(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("item_id")
    val itemId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("weight")
    val weight: Int,
)
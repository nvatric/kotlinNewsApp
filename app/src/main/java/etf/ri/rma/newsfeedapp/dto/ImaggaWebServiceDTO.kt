package etf.ri.rma.newsfeedapp.dto

import com.google.gson.annotations.SerializedName

class ImaggaResponse(
    @SerializedName("result") val result: ResultData
){
    val tags: List<String>
        get() = result.tags.map { it.tag.en }
}

data class ResultData(
    val tags: List<TagData>
)

data class TagData(
    val tag: TagTranslation
)

data class TagTranslation(
    val en: String
)

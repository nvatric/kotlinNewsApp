package etf.ri.rma.newsfeedapp.dto

import etf.ri.rma.newsfeedapp.model.NewsItem
import com.google.gson.annotations.SerializedName
import java.util.*

data class NewsResponse(
    @SerializedName("data") val data: List<Article>
)

data class Article(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("keywords") val keywords: String?,
    @SerializedName("snippet") val snippet: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("source") val rawSource: Any?,
    @SerializedName("categories") val categories: List<String>?
) {
    val source: Source?
        get() {
            return when (rawSource) {
                is Map<*, *> -> {
                    val name = rawSource["name"] as? String
                    val id = rawSource["id"] as? String
                    Source(id, name)
                }
                is String -> Source(null, rawSource)
                else -> null
            }
        }
}

data class Source(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)

fun Article.toNewsItem(): NewsItem {
    val category = categories?.firstOrNull() ?: "Uncategorized"
    return NewsItem(
        uuid = this.uuid,
        title = this.title ?: "No title",
        snippet = this.snippet ?: this.description ?: "No description",
        imageUrl = this.imageUrl,
        category = category,
        isFeatured = false,
        source = this.source?.name ?: "Unknown",
        publishedDate = formatPublishedDate(this.publishedAt),
        imageTags = ArrayList()
    )
}

fun formatPublishedDate(publishedAt: String?): String {
    return publishedAt?.let {
        val parts = it.split("T")[0].split("-")
        if (parts.size == 3) {
            "${parts[2]}-${parts[1]}-${parts[0]}"
        } else {
            "Unknown Date"
        }
    } ?: "Unknown Date"
}

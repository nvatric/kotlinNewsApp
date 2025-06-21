package etf.ri.rma.newsfeedapp.model

import androidx.room.*

@Entity(
    tableName = "News",
    indices = [Index(value = ["uuid"], unique = true)]
)

data class News(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var uuid: String = "",
    var title: String = "",
    var snippet: String = "",
    var imageUrl: String? = null,
    var category: String = "",
    var isFeatured: Boolean = false,
    var source: String = "",
    var publishedDate: String = "",
    @Ignore var tags: List<TagEntity> = emptyList()
) {
    constructor() : this(0, "", "", "", null, "", false, "", "")
}



@Entity(tableName = "Tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: String
)

@Entity(
    tableName = "NewsTags",
    primaryKeys = ["newsId", "tagId"],
    foreignKeys = [
        ForeignKey(entity = News::class, parentColumns = ["id"], childColumns = ["newsId"]),
        ForeignKey(entity = TagEntity::class, parentColumns = ["id"], childColumns = ["tagId"])
    ]
)
data class NewsTagsEntity(
    val newsId: Int,
    val tagId: Int
)

fun News.copyTags(tags: List<TagEntity>): News {
    return this.copy()
}

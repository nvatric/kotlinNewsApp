package etf.ri.rma.newsfeedapp.data

import androidx.room.*
import etf.ri.rma.newsfeedapp.model.*

@Dao
interface SavedNewsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNews(news: News): Long

    @Query("SELECT * FROM News")
    fun getAllNews(): List<News>

    @Query("SELECT * FROM News WHERE category = :category")
    fun getNewsWithCategory(category: String): List<News>

    @Query("SELECT * FROM Tags WHERE value = :tag LIMIT 1")
    fun getTagByValue(tag: String): TagEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTag(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNewsTagCrossRef(ref: NewsTagsEntity)

    @Query("SELECT id FROM News WHERE uuid = :uuid LIMIT 1")
    fun getNewsIdByUUID(uuid: String): Int?

    @Query("SELECT * FROM Tags")
    fun getAllTags(): List<TagEntity>

    @Query(
        """
        SELECT Tags.value FROM Tags
        INNER JOIN NewsTags ON Tags.id = NewsTags.tagId
        WHERE NewsTags.newsId = :newsId
        """
    )
    fun getTags(newsId: Int): List<String>

    @Query(
        """
        SELECT DISTINCT News.* FROM News
        INNER JOIN NewsTags ON News.id = NewsTags.newsId
        INNER JOIN Tags ON Tags.id = NewsTags.tagId
        WHERE Tags.value IN (:tags)
        ORDER BY publishedDate DESC
        """
    )
    fun getSimilarNews(tags: List<String>): List<News>
    @Query("SELECT * FROM News WHERE uuid = :uuid LIMIT 1")
    fun getNewsByUUID(uuid: String): News?

    @Query("SELECT * FROM News WHERE title = :title AND publishedDate = :publishedDate LIMIT 1")
    fun getNewsByTitleAndDate(title: String, publishedDate: String): News?

    @Transaction
    fun addTags(tags: List<String>, newsId: Int): Int {
        var newTags = 0
        for (tagValue in tags) {
            val existingTag = getTagByValue(tagValue)
            val tagId = if (existingTag != null) {
                existingTag.id
            } else {
                val id = insertTag(TagEntity(value = tagValue)).toInt()
                if (id != -1) newTags++
                id
            }
            insertNewsTagCrossRef(NewsTagsEntity(newsId, tagId))
        }
        return newTags
    }

    @Transaction
    suspend fun saveNews(news: News): Boolean {
        val existingByUUID = getNewsByUUID(news.uuid)
        val existingByTitleDate = getNewsByTitleAndDate(news.title, news.publishedDate)
        if (existingByUUID != null || existingByTitleDate != null) return false
        return insertNews(news) != -1L
    }

    @Transaction
    fun allNews(): List<News> {
        return getAllNews().map {
            val tags = getTags(it.id)
            it.copy(tags = tags.map { tag -> TagEntity(value = tag) })
        }
    }
}

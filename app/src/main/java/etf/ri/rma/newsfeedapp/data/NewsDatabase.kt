package etf.ri.rma.newsfeedapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import etf.ri.rma.newsfeedapp.model.News
import etf.ri.rma.newsfeedapp.model.NewsTagsEntity
import etf.ri.rma.newsfeedapp.model.TagEntity

@Database(
    entities = [News::class, TagEntity::class, NewsTagsEntity::class],
    version = 2,
    exportSchema = false
)

abstract class NewsDatabase : RoomDatabase() {
    abstract fun savedNewsDAO(): SavedNewsDAO
}

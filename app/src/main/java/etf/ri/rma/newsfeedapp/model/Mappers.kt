package etf.ri.rma.newsfeedapp.model

fun NewsItem.toNews(): News {
    return News(
        uuid = this.uuid,
        title = this.title,
        snippet = this.snippet,
        imageUrl = this.imageUrl,
        category = this.category,
        isFeatured = this.isFeatured,
        source = this.source,
        publishedDate = this.publishedDate,
        tags = this.imageTags
    )
}


fun News.toNewsItem(): NewsItem {
    return NewsItem(
        uuid = this.uuid,
        title = this.title,
        snippet = this.snippet,
        imageUrl = this.imageUrl,
        category = this.category,
        isFeatured = this.isFeatured,
        source = this.source,
        publishedDate = this.publishedDate,
        imageTags = ArrayList(this.tags)
    )
}



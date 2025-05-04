package etf.ri.rma.newsfeedapp.model

data class FilterData(
    val category: String = "All",
    val dateRange: String? = null,
    val unwantedWords: List<String> = emptyList(),
)
package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.FilterData
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun NewsFeedScreen(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    var filters by remember {
        mutableStateOf(FilterData(category = "All", dateRange = null, unwantedWords = emptyList()))
    }

    // OVO REAGUJE NA PROMJENE I AŽURIRA FILTAR
    LaunchedEffect(
        savedStateHandle?.get<String>("category"),
        savedStateHandle?.get<String>("dateRange"),
        savedStateHandle?.get<Array<String>>("unwantedWords")
    ) {
        val newCategory = savedStateHandle?.get<String>("category") ?: "All"
        val newFilters = FilterData(
            category = newCategory,
            dateRange = savedStateHandle?.get<String>("dateRange"),
            unwantedWords = savedStateHandle?.get<Array<String>>("unwantedWords")?.toList() ?: emptyList()
        )
        filters = newFilters
    }//
    val allNews = NewsData.getAllNews()
    val filteredNews = allNews.filter {
        (filters.category == "All" || it.category == filters.category) &&
                (filters.dateRange?.let { range -> isDateInRange(it.publishedDate, range) } ?: true) &&
                filters.unwantedWords.none { word ->
                    it.title.contains(word, ignoreCase = true) || it.snippet.contains(word, ignoreCase = true)
                }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        FilterSection(
            selectedCategories = setOf(filters.category),
            onCategorySelected = { newCategory ->
                filters = filters.copy(category = newCategory)
            },
            navController = navController,
            filters=filters
        )

        Spacer(modifier = Modifier.height(8.dp))

<<<<<<< Updated upstream
        SortSel(
            selectedSortOption = selectedSortOption,
            onSortOptionSelected = { sortOption ->
                selectedSortOption = sortOption
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (sortedNews.isEmpty()) {
            MessageCard(poruka = "Nema pronađenih vijesti za odabrane filtere")
=======
        if (filteredNews.isEmpty()) {
            Text("Nema pronađenih vijesti u kategoriji ${filters.category}")
>>>>>>> Stashed changes
        } else {
            NewsList(
                newsItem = filteredNews,
                navController = navController
            )
        }
    }
}


// **Popravljena funkcija za filtriranje po datumu**
fun isDateInRange(date: String, dateRange: String?): Boolean {
    if (dateRange.isNullOrEmpty()) return true // Ako korisnik nije odabrao datume, prikazujemo sve vijesti

    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Tačan format za poređenje datuma
    val newsDate = format.parse(date) ?: return false // Parsiramo datum vijesti

    // Razdvajamo opseg datuma na početni i krajnji datum
    val rangeParts = dateRange.split(";")
    if (rangeParts.size != 2) return false // Ako format nije ispravan, ne filtriramo

    val startDate = format.parse(rangeParts[0]) ?: return false
    val endDate = format.parse(rangeParts[1]) ?: return false

    // Provjeravamo da li vijest spada unutar opsega datuma
    return (newsDate.after(startDate) && newsDate.before(endDate)) || newsDate == startDate || newsDate == endDate
}
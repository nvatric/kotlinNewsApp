package etf.ri.rma.newsfeedapp.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var unwantedWord by remember { mutableStateOf("") }
    var unwantedWords by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }

    val dateRangePickerState = rememberDateRangePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedRange by remember { mutableStateOf<Pair<String, String>?>(null) }
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val coroutineScope = rememberCoroutineScope()
    val newsDAO = remember { NewsDAO() }

    val categoryMap = mapOf(
        "Politika" to "politics",
        "Sport" to "sports",
        "Nauka/tehnologija" to "technology",
        "Svijet" to "general",
        "All" to "general"
    )

    fun mapToApiCategory(displayCategory: String): String {
        return categoryMap[displayCategory] ?: "general"
    }

    LaunchedEffect(navController) {
        selectedCategory = navController.previousBackStackEntry?.savedStateHandle?.get<String>("selectedCategory") ?: "All"
        selectedRange = navController.previousBackStackEntry?.savedStateHandle?.get<Pair<String, String>>("selectedRange")
        unwantedWords = navController.previousBackStackEntry?.savedStateHandle?.get<List<String>>("unwantedWords") ?: emptyList()
    }

    fun fetchCategoryNews(category: String) {
        if (category == "All") return

        coroutineScope.launch {
            isLoading = true
            try {
                val apiCategory = mapToApiCategory(category)
                Log.d("FILTER_SCREEN", "Fetching news for category: $category (API: $apiCategory)")

                val newStories = newsDAO.getTopStoriesByCategory(apiCategory)
                Log.d("FILTER_SCREEN", "Successfully fetched ${newStories.size} new stories for category: $category")

                navController.previousBackStackEntry?.savedStateHandle?.set("categoryFetched_$apiCategory", true)

            } catch (e: Exception) {
                Log.e("FILTER_SCREEN", "Error fetching news for category $category: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val start = dateRangePickerState.selectedStartDateMillis?.let {
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        val end = dateRangePickerState.selectedEndDateMillis?.let {
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        if (start != null && end != null) {
                            selectedRange = Pair(
                                start.format(outputFormatter),
                                end.format(outputFormatter)
                            )
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(state = dateRangePickerState)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Text(
            text = "Kategorije vijesti",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val categories = listOf(
            "All" to "filter_chip_all",
            "Politika" to "filter_chip_pol",
            "Sport" to "filter_chip_spo",
            "Nauka/tehnologija" to "filter_chip_sci",
            "Svijet" to "filter_chip_svi"
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { (category, testTag) ->
                CategoryChip(
                    category = category,
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        Log.d("FILTER_SCREEN", "Category selected: $category")
                        fetchCategoryNews(category)
                    },
                    testTag = testTag
                )
            }
        }

        Divider()

        Text(text = "Vremenski raspon", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = selectedRange?.let { "${it.first} - ${it.second}" } ?: "Nije odabran",
                modifier = Modifier
                    .testTag("filter_daterange_display")
                    .align(Alignment.CenterVertically)
            )
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.testTag("filter_daterange_button")
            ) {
                Text("Odaberite datume")
            }
        }

        Divider()

        Text(text = "Neželjene riječi", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                value = unwantedWord,
                onValueChange = { unwantedWord = it },
                modifier = Modifier
                    .testTag("filter_unwanted_input")
                    .weight(1f),
                label = { Text("Unesite riječ") },
                singleLine = true
            )
            Button(
                onClick = {
                    val word = unwantedWord.trim()
                    if (word.isNotEmpty() && unwantedWords.none { it.equals(word, ignoreCase = true) }) {
                        unwantedWords = unwantedWords + word
                        unwantedWord = ""
                    }
                },
                modifier = Modifier.testTag("filter_unwanted_add_button")
            ) {
                Text("Dodaj")
            }
        }

        if (unwantedWords.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .testTag("filter_unwanted_list")
                    .heightIn(max = 150.dp)
            ) {
                items(unwantedWords) { word ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = word, modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = {
                                unwantedWords = unwantedWords.filter { it != word }
                            }
                        ) {
                            Text("Ukloni", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    selectedCategory = "All"
                    selectedRange = null
                    unwantedWords = emptyList()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Resetuj")
            }

            Button(
                onClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        remove<String>("selectedCategory")
                        remove<Pair<String, String>>("selectedRange")
                        remove<List<String>>("unwantedWords")

                        set("selectedCategory", selectedCategory)
                        set("selectedRange", selectedRange)
                        set("unwantedWords", unwantedWords)
                    }

                    Log.d("FILTER_SCREEN", "Applied filters - Category: $selectedCategory, Range: $selectedRange, Unwanted: $unwantedWords")
                    navController.popBackStack()
                },
                modifier = Modifier
                    .testTag("filter_apply_button")
                    .weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Primijeni")
                }
            }
        }
    }
}

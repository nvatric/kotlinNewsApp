package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FilterScreen(navController: NavController) {
    // Preuzimanje podataka iz savedStateHandle pomoću LaunchedEffect
    var selectedCategory by remember { mutableStateOf("All") }
    var dateFrom by remember { mutableStateOf("") }
    var dateTo by remember { mutableStateOf("") }
    var unwantedWordsList by remember { mutableStateOf(listOf<String>()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var unwantedWord by remember { mutableStateOf("") }

    // Koristimo LaunchedEffect da bismo preuzeli vrednosti iz savedStateHandle
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    LaunchedEffect(
        savedStateHandle?.get<String>("category"),
        savedStateHandle?.get<String>("dateRange"),
        savedStateHandle?.get<Array<String>>("unwantedWords")
    ) {
        val newCategory = savedStateHandle?.get<String>("category") ?: "All"
        val newDateRange = savedStateHandle?.get<String>("dateRange")
        val newUnwantedWords = savedStateHandle?.get<Array<String>>("unwantedWords")?.toList() ?: emptyList()

        // Ažuriramo stanje sa preuzetim podacima
        selectedCategory = newCategory
        dateFrom = newDateRange?.split(";")?.get(0) ?: ""
        dateTo = newDateRange?.split(";")?.get(1) ?: ""
        unwantedWordsList = newUnwantedWords
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Odaberi kategoriju:")
        val categories = listOf(
            "All" to "filter_chip_all",
            "Politika" to "filter_chip_pol",
            "Sport" to "filter_chip_spo",
            "Nauka/tehnologija" to "filter_chip_sci"
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { (category, testTag) ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    modifier = Modifier.testTag(testTag)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 PRIKAZ OPSEGA DATUMA
        Text(
            text = if (dateFrom.isNotEmpty() && dateTo.isNotEmpty()) "$dateFrom;$dateTo" else "Odaberite opseg datuma",
            modifier = Modifier.testTag("filter_daterange_display")
        )

        // 🔹 Dugme za otvaranje DateRangePicker-a
        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.testTag("filter_daterange_button")
        ) {
            Text("Odaberi datum")
        }

        if (showDatePicker) {
            DateRangePickerDialog(
                onDateSelected = { start, end ->
                    dateFrom = start
                    dateTo = end
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Unos nepoželjnih riječi
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                value = unwantedWord,
                onValueChange = { unwantedWord = it },
                modifier = Modifier.testTag("filter_unwanted_input"),
                placeholder = { Text("Nepoželjna riječ") }
            )
            Button(
                onClick = {
                    val word = unwantedWord.trim()
                    if (word.isNotEmpty() && unwantedWordsList.none { it.equals(word, ignoreCase = true) }) {
                        unwantedWordsList = unwantedWordsList + word
                    }
                    unwantedWord = ""
                },
                modifier = Modifier.testTag("filter_unwanted_add_button")
            ) {
                Text("Dodaj")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .testTag("filter_unwanted_list")
                .verticalScroll(rememberScrollState())
        ) {
            unwantedWordsList.forEach { word ->
                Text(word)
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle?.apply {
                        set("category", selectedCategory)
                        set("dateRange", if (dateFrom.isNotBlank() && dateTo.isNotBlank()) "$dateFrom;$dateTo" else null)
                        set("unwantedWords", unwantedWordsList.toTypedArray()) // Pretvaramo listu u niz
                    }

                navController.popBackStack()
            },
            //------------------------------------------



            modifier = Modifier.testTag("filter_apply_button")
        ) {
            Text("Primijeni filtere")
        }
    }
}
package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.model.FilterData

@Composable
fun FilterSection(
    selectedCategories: Set<String>,
    onCategorySelected: (String) -> Unit,
    navController: NavController,
    filters : FilterData
) {
    val categories = listOf(
        "All" to "filter_chip_all",
        "Politika" to "filter_chip_pol",
        "Sport" to "filter_chip_spo",
        "Nauka/tehnologija" to "filter_chip_sci",
        "Svijet" to "filter_chip_empty"
    )


    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { (category, testTag) ->
            val selected = selectedCategories.contains(category)
            FilterChip(
                selected = selected,
                onClick = { onCategorySelected(category) }, // **Ažurira filter čim klikneš!**
                label = { Text(category) },
                modifier = Modifier.testTag(testTag)
            )
        }
    }
    FilterChip(
            selected = false,
    onClick = {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set("category", filters.category)
            set("dateRange", filters.dateRange)
            set("unwantedWords", filters.unwantedWords.toTypedArray()) // Pretvaramo listu u niz
        }
        // Navigirajte ka ekranu sa filtrima
        navController.navigate("filters")
              },
    label = { Text("Više filtera ...") },
    modifier = Modifier.testTag("filter_chip_more")
    )
}







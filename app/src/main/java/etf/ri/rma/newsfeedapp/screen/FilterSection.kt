package etf.ri.rma.newsfeedapp.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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


@Composable
fun FilterSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    navController: NavController
) {
    val categories = listOf(
        "All" to "filter_chip_all",
        "Politika" to "filter_chip_pol",
        "Sport" to "filter_chip_spo",
        "Nauka/tehnologija" to "filter_chip_sci",
        "Svijet" to "filter_chip_empty"
    )
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
    Column {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { (category, testTag) ->
                val selected = selectedCategory == category
                FilterChip(
                    selected = selected,
                    onClick = {
                        Log.d("FILTER_SECTION", "Selected category: $category, API: ${mapToApiCategory(category)}")
                        onCategorySelected(category)
                    },
                    label = { Text(category) },
                    modifier = Modifier.testTag(testTag)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FilterChip(
            selected = false,
            onClick = {
                navController.navigate("filters")
            },
            label = { Text("Više filtera ...") },
            modifier = Modifier
                .padding(start = 8.dp)
                .testTag("filter_chip_more")
        )
    }
}

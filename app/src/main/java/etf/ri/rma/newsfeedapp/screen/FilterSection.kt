package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items

@Composable
fun FilterSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        "Sve" to "filter_chip_all",
        "Politika" to "filter_chip_pol",
        "Sport" to "filter_chip_spo",
        "Nauka/tehnologija" to "filter_chip_sci",
        "Svijet" to "filter_chip_empty"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { pair->
            val (category, testTag) =pair
            FilterChipItem(
                category = category,
                selected = selectedCategory == category,
                onClick = onCategorySelected,
                testTag = testTag
            )
        }
    }
}

@Composable
fun FilterChipItem(
    category: String,
    selected: Boolean,
    onClick: (String) -> Unit,
    testTag: String
) {
    FilterChip(
        selected = selected,
        onClick = {
            if (!selected) onClick(category)
        },
        label = { Text(category) },
        modifier = Modifier.testTag(testTag)
    )
}

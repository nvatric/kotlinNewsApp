package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun SortSel(
    selectedSortOption: String?,
    onSortOptionSelected: (String?) -> Unit
) {
    val sortOptions = listOf(
        "Datum ⇩" to "sort_chip_date_asc",
        "Datum ⇧" to "sort_chip_date_desc"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sortOptions.forEach { (label, testTag) ->
            FilterChip(
                selected = selectedSortOption == label,
                onClick = {
                    onSortOptionSelected(
                        if (selectedSortOption == label) null else label
                    )
                },
                label = { Text(label) },
                modifier = Modifier.testTag(testTag)
            )
        }
    }
}

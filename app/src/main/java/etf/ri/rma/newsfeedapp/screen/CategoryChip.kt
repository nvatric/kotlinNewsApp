package etf.ri.rma.newsfeedapp.screen

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun CategoryChip(
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
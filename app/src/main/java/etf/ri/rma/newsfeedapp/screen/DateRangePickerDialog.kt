package etf.ri.rma.newsfeedapp.screen

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    onDateSelected: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDateRangePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val startDate = datePickerState.selectedStartDateMillis?.let { formatter.format(Date(it)) }
                val endDate = datePickerState.selectedEndDateMillis?.let { formatter.format(Date(it)) }
                if (startDate != null && endDate != null) {
                    onDateSelected(startDate, endDate)
                }
            }) {
                Text("Potvrdi")
            }
        }
    ) {
        DateRangePicker(state = datePickerState)
    }
}
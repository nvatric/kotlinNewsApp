package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.ui.NewsDetailsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val newsDAO = remember { NewsDAO() }

    MaterialTheme {
        Surface {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    NewsFeedScreen(navController, newsDAO)
                }

                composable("filters") {
                    FilterScreen(navController = navController)
                }

                composable("details/{uuid}") { backStackEntry ->
                    val newsId = backStackEntry.arguments?.getString("uuid") ?: ""
                    NewsDetailsScreen(newsId = newsId, navController = navController, newsDAO = newsDAO)
                }
            }
        }
    }
}
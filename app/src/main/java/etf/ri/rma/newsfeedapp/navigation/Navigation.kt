package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.data.NewsDatabase
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.ui.NewsDetailsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val database = remember {
        Room.databaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java,
            "news-db"
        ).fallbackToDestructiveMigration().build()
    }
    val savedNewsDAO = database.savedNewsDAO()

    val newsDAO = remember { NewsDAO(savedNewsDAO) }

    MaterialTheme {
        Surface {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    NewsFeedScreen(
                        navController = navController,
                        newsDAO = newsDAO,
                        savedNewsDAO = savedNewsDAO
                    )
                }


                composable("filters") {
                    FilterScreen(navController = navController, newsDAO = newsDAO)
                }


                composable("details/{uuid}") { backStackEntry ->
                    val newsId = backStackEntry.arguments?.getString("uuid") ?: ""
                    NewsDetailsScreen(
                        newsId = newsId,
                        navController = navController,
                        newsDAO = newsDAO,
                        savedNewsDAO = savedNewsDAO
                    )
                }
            }
        }
    }
}

package etf.ri.rma.newsfeedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "home") {
                        composable("home") { NewsFeedScreen(navController) }
                        composable("filters") { FilterScreen(navController) }
                        composable("details/{id}") { backStackEntry ->
                            val newsId = backStackEntry.arguments?.getString("id") ?: ""
                            NewsDetailsScreen(newsId = newsId, navController = navController)
                        }
                    }
                    //morat cu ti comitat na spiralu 1 sve neeeee, zastoo, jer si sve radila na spirali 1
                    //jooj  sta sad ostavi sve ja cu nista , hvalaaa ti snalazit cu se, probaj MERGE spiralu

                }
            }
        }
    }
}
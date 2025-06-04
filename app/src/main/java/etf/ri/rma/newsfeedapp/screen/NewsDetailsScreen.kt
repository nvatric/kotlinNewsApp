package etf.ri.rma.newsfeedapp.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(
    newsId: String,
    navController: NavController,
    newsDAO: NewsDAO
) {
    val imagaDAO = remember { ImagaDAO() }

    var newsItem by remember { mutableStateOf<NewsItem?>(null) }
    var imageTags by remember { mutableStateOf<List<String>>(emptyList()) }
    var similarStories by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(newsId) {
        isLoading = true
        try {
            val allStories = newsDAO.getAllStories()
            newsItem = allStories.find { it.uuid == newsId }

            if (newsItem?.imageUrl != null) {
                try {
                    imageTags = imagaDAO.getTags(newsItem!!.imageUrl!!)
                } catch (_: Exception) {
                    imageTags = emptyList()
                }
            }

            similarStories = newsDAO.getSimilarStories(newsId)

        } catch (e: Exception) {
            Log.e("NewsDetailsScreen", "Greska: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalji vijesti") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
                    }
                }
            )
        }
    ) {
        padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (newsItem != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    Text(newsItem!!.title, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    AsyncImage(
                        model = newsItem!!.imageUrl,
                        contentDescription = "Slika vijesti",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = newsItem!!.category)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Tagovi slike:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))

                    if (imageTags.isNotEmpty()) {
                        imageTags.forEach { tag ->
                            Text("• $tag", style = MaterialTheme.typography.bodySmall)
                        }
                    } else {
                        Text("Nema dostupnih tagova.")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Slične vijesti:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))

                    if (similarStories.isNotEmpty()) {
                        similarStories.take(3).forEach { story ->
                            Text(
                                text = "• ${story.title}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        navController.navigate("details/${story.uuid}") {
                                            popUpTo("details/${newsId}") { inclusive = true }
                                        }
                                    }
                            )
                        }
                    } else {
                        Text("Nema sličnih vijesti.")
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Vijest nije pronađena")
            }
        }
    }
}

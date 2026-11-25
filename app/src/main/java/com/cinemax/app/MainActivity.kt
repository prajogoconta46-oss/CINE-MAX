package com.cinemax.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

private val Black = Color(0xFF090909)
private val Panel = Color(0xFF171717)
private val Gold = Color(0xFFD6AE55)
private val SoftWhite = Color(0xFFF5F1E8)

data class Movie(val title: String, val genre: String, val year: String, val description: String)

private val movies = listOf(
    Movie("A Última Missão", "Ação", "2025", "Uma missão inesperada muda tudo."),
    Movie("Risos da Cidade", "Comédia", "2024", "Uma comédia leve para toda a família."),
    Movie("Noite Sombria", "Terror", "2025", "Mistérios aparecem depois da meia-noite."),
    Movie("Além das Estrelas", "Ficção científica", "2024", "Uma viagem para além do conhecido."),
    Movie("Coração em Viagem", "Romance", "2023", "Duas histórias se cruzam por acaso."),
    Movie("Mundo Selvagem", "Documentário", "2025", "Uma jornada pela natureza.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CineMaxApp() }
    }
}

@Composable
fun CineMaxApp() {
    var selectedTab by remember { mutableStateOf("Início") }
    var search by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Todos") }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    var playing by remember { mutableStateOf<Movie?>(null) }

    val genres = listOf("Todos", "Ação", "Comédia", "Terror", "Romance", "Ficção científica", "Documentário")
    val visibleMovies = movies.filter {
        (selectedTab != "Favoritos" || favorites.contains(it.title)) &&
        (selectedGenre == "Todos" || it.genre == selectedGenre) &&
        (it.title.contains(search, ignoreCase = true) || it.genre.contains(search, ignoreCase = true))
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Gold, background = Black, surface = Panel,
        onPrimary = Black, onBackground = SoftWhite, onSurface = SoftWhite
    )) {
        Scaffold(
            containerColor = Black,
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Gold) {
                    NavigationBarItem(
                        selected = selectedTab == "Início",
                        onClick = { selectedTab = "Início" },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                        label = { Text("Início") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "Favoritos",
                        onClick = { selectedTab = "Favoritos" },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                        label = { Text("Favoritos") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "Perfil",
                        onClick = { selectedTab = "Perfil" },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                }
            }
        ) { padding ->
            if (playing != null) {
                VideoScreen(movie = playing!!, onBack = { playing = null })
            } else if (selectedTab == "Perfil") {
                Column(Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
                    Text("CINE MAX", color = Gold, fontSize = 30.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(18.dp))
                    Text("Seu cinema, do seu jeito.", color = SoftWhite, fontSize = 18.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("Versão inicial • Catálogo demonstrativo", color = Color.LightGray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text("CINE MAX", color = Gold, fontSize = 30.sp, fontWeight = FontWeight.Black)
                        Text("SEU CINEMA EM UM SÓ LUGAR", color = Color.LightGray, fontSize = 10.sp, letterSpacing = 2.sp)
                    }
                    item {
                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar filmes e gêneros") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Gold, unfocusedBorderColor = Color.DarkGray,
                                focusedTextColor = SoftWhite, unfocusedTextColor = SoftWhite
                            )
                        )
                    }
                    item {
                        Text(if (selectedTab == "Favoritos") "MINHA LISTA" else "ESCOLHA SEU GÊNERO",
                            color = Gold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            genres.take(4).forEach { genre ->
                                FilterChip(
                                    selected = selectedGenre == genre,
                                    onClick = { selectedGenre = genre },
                                    label = { Text(genre) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Gold, selectedLabelColor = Black
                                    )
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            genres.drop(4).forEach { genre ->
                                FilterChip(
                                    selected = selectedGenre == genre,
                                    onClick = { selectedGenre = genre },
                                    label = { Text(genre) }
                                )
                            }
                        }
                    }
                    item {
                        Text("EM DESTAQUE", color = Gold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Box(
                            Modifier.fillMaxWidth().height(145.dp)
                                .background(Panel, RoundedCornerShape(18.dp)).padding(18.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column {
                                Text("NOITE DE ESTREIA", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
                                Text("Descubra sua próxima história", color = SoftWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("Explore o catálogo CINE MAX", color = Color.LightGray, fontSize = 12.sp)
                            }
                        }
                    }
                    item { Text("CATÁLOGO", color = Gold, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    if (visibleMovies.isEmpty()) {
                        item { Text("Nenhum título encontrado.", color = Color.LightGray) }
                    }
                    items(visibleMovies) { movie ->
                        MovieCard(
                            movie = movie,
                            favorite = favorites.contains(movie.title),
                            onFavorite = {
                                favorites = if (favorites.contains(movie.title)) favorites - movie.title
                                else favorites + movie.title
                            },
                            onPlay = { playing = movie }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieCard(movie: Movie, favorite: Boolean, onFavorite: () -> Unit, onPlay: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(width = 76.dp, height = 100.dp)
                .background(Color(0xFF302719), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) { Text("▶", color = Gold, fontSize = 27.sp) }
        Column(Modifier.weight(1f).padding(horizontal = 13.dp)) {
            Text(movie.title, color = SoftWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("${movie.genre} • ${movie.year}", color = Gold, fontSize = 11.sp)
            Spacer(Modifier.height(5.dp))
            Text(movie.description, color = Color.LightGray, fontSize = 12.sp, maxLines = 2)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onPlay, colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Black)) {
                Text("Assistir")
            }
        }
        IconButton(onClick = onFavorite) {
            Icon(if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favoritar", tint = Gold)
        }
    }
}

@Composable
private fun VideoScreen(movie: Movie, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Black).padding(16.dp)) {
        Text("‹  VOLTAR", color = Gold, modifier = Modifier.clickable { onBack() }.padding(8.dp))
        Text(movie.title, color = SoftWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        DemoPlayer()
        Spacer(Modifier.height(12.dp))
        Text("Vídeo público de demonstração", color = Gold, fontSize = 13.sp)
        Text("Este player serve para testar a reprodução. Para filmes completos, adicione vídeos próprios ou licenciados.",
            color = Color.LightGray, fontSize = 12.sp)
    }
}

@Composable
private fun DemoPlayer() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
            prepare()
            playWhenReady = false
        }
    }
    DisposableEffect(player) { onDispose { player.release() } }
    AndroidView(
        factory = { ctx -> PlayerView(ctx).apply { this.player = player } },
        modifier = Modifier.fillMaxWidth().height(220.dp)
    )
}

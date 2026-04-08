package presentation.locationdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

/**
 * Composable qui affiche les détails d'un lieu.
 */
@Composable
fun LocationDetailScreen(
    viewModel: LocationDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LocationDetailContent(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
            if (action is LocationDetailUiAction.OnBackClicked) {
                onNavigateBack()
            }
        }
    )
}

/**
 * Composable qui ne contient aucune logique métier, que de l'affichage.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailContent(
    state: LocationDetailUiState,
    onAction: (LocationDetailUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détail de la Location") },
                navigationIcon = {
                    IconButton(onClick = { onAction(LocationDetailUiAction.OnBackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is LocationDetailUiState.Loading -> CircularProgressIndicator()
                is LocationDetailUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { onAction(LocationDetailUiAction.OnRetryClicked) }) {
                            Text("Réessayer")
                        }
                    }
                }
                is LocationDetailUiState.Success -> {
                    val location = state.location
                    Card(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(text = location.name, style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "ID: ${location.id}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Type: ${location.type}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Dimension: ${location.dimension}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Nombre de résidents: ${location.residentCount}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}
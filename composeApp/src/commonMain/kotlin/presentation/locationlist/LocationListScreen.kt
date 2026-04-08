package presentation.locationlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.Location

/**
 * Composable qui écoute le ViewModel et gère la navigation vers le détail (pour le Mobile).
 */
@Composable
fun LocationListScreen(
    viewModel: LocationListViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    // variable réactive
    val state by viewModel.uiState.collectAsState()

    LocationListContent(
        state = state,
        onAction = { action ->
            // On prévient le ViewModel
            viewModel.onAction(action)

            // Si c'est un clic, on prévient aussi la navigation externe (Mobile)
            if (action is LocationListUiAction.OnLocationClicked) {
                onNavigateToDetail(action.locationId)
            }
        }
    )
}

/**
 * Composable qui ne contient aucune logique métier, que de l'affichage.
 */
@Composable
fun LocationListContent(
    state: LocationListUiState,
    onAction: (LocationListUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("Rick & Morty Locations") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is LocationListUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is LocationListUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { onAction(LocationListUiAction.OnRetryClicked) }) {
                            Text("Réessayer")
                        }
                    }
                }
                is LocationListUiState.Success -> {
                    // Affichage de la liste
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.locations) { location ->
                            LocationItem(
                                location = location,
                                onClick = { onAction(LocationListUiAction.OnLocationClicked(it)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable qui représente visuellement une seule ligne de la liste.
 */
@Composable
fun LocationItem(
    location: Location,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(location.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = location.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Type : ${location.type}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Dimension : ${location.dimension}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Résidents : ${location.residentCount}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
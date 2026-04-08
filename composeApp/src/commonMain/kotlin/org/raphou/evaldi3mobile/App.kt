package org.raphou.evaldi3mobile


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

import presentation.locationdetail.LocationDetailScreen
import presentation.locationdetail.LocationDetailViewModel
import presentation.locationlist.LocationListScreen
import presentation.locationlist.LocationListViewModel
import presentation.navigation.Navigator
import presentation.navigation.Screen

@Composable
fun App() {
    val soundManager = crossnative.rememberSoundManager()
    KoinApplication(application = {
        modules(di.appModule)
    }) {
        MaterialTheme {
            // BoxWithConstraints permet de connaître la taille disponible de l'écran !
            BoxWithConstraints {
                if (maxWidth < 800.dp) {
                    // ==========================================
                    // MODE MOBILE : Navigation classique (Écran par écran)
                    // ==========================================
                    val currentScreen by Navigator.currentScreen.collectAsState()

                    when (val screen = currentScreen) {
                        is Screen.LocationList -> {
                            val viewModel = koinInject<LocationListViewModel>()
                            LocationListScreen(
                                viewModel = viewModel,
                                onNavigateToDetail = { id ->
                                    soundManager.playSound()
                                    Navigator.navigateTo(Screen.LocationDetail(id)) }
                            )
                        }
                        is Screen.LocationDetail -> {
                            val viewModel = koinInject<LocationDetailViewModel>(
                                parameters = { org.koin.core.parameter.parametersOf(screen.locationId) }
                            )
                            LocationDetailScreen(
                                viewModel = viewModel,
                                onNavigateBack = { Navigator.goBack() }
                            )
                        }
                    }
                } else {
                    // ==========================================
                    // MODE DESKTOP : Master-Detail (Les deux sur le même écran)
                    // ==========================================
                    var selectedLocationId by remember { mutableStateOf<Int?>(null) }

                    Row(modifier = Modifier.fillMaxSize()) {
                        // gauche donc la liste
                        Box(modifier = Modifier.weight(1f)) {
                            val listViewModel = koinInject<LocationListViewModel>()
                            LocationListScreen(
                                viewModel = listViewModel,
                                onNavigateToDetail = { id ->
                                    soundManager.playSound()
                                    selectedLocationId = id
                                }
                            )
                        }

                        // Séparateur visuel
                        VerticalDivider(modifier = Modifier.width(1.dp))

                        // droite donc le détail
                        Box(modifier = Modifier.weight(2f)) {
                            if (selectedLocationId != null) {
                                // Le bloc `key` force Koin à recréer un ViewModel à chaque fois que l'ID change
                                key(selectedLocationId) {
                                    val detailViewModel = koinInject<LocationDetailViewModel>(
                                        parameters = { org.koin.core.parameter.parametersOf(selectedLocationId) }
                                    )
                                    LocationDetailScreen(
                                        viewModel = detailViewModel,
                                        onNavigateBack = { selectedLocationId = null } // Ferme le détail
                                    )
                                }
                            } else {
                                // Pas de détail ouvert
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Sélectionnez un lieu à gauche",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
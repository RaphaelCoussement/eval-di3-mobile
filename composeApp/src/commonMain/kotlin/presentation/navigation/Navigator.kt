package presentation.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class Screen {
    data object LocationList : Screen()
    data class LocationDetail(val locationId: Int) : Screen()
}

// Modèle de données pour la navigation
object Navigator {
    private val _currentScreen = MutableStateFlow<Screen>(Screen.LocationList)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun goBack() {
        // le retour ramène toujours à la liste pour simplifier
        _currentScreen.value = Screen.LocationList
    }
}
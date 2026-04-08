package presentation.locationlist

import domain.model.Location

/**
 * Représente tous les états possibles de l'écran.
 */
sealed interface LocationListUiState {
    data object Loading : LocationListUiState
    data class Success(val locations: List<Location>) : LocationListUiState
    data class Error(val message: String) : LocationListUiState
}
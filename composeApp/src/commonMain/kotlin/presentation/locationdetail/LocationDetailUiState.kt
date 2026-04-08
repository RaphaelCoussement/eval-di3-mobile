package presentation.locationdetail

import domain.model.Location

/**
 * Représente tous les états possibles de l'écran.
 */
sealed interface LocationDetailUiState {
    data object Loading : LocationDetailUiState
    data class Success(val location: Location) : LocationDetailUiState
    data class Error(val message: String) : LocationDetailUiState
}
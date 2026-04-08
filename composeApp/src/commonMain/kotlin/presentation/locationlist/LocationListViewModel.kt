package presentation.locationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationListViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    // L'état interne (privé). Seul ce fichier a le droit de le modifier.
    private val _uiState = MutableStateFlow<LocationListUiState>(LocationListUiState.Loading)

    // L'état public (lecture seule). L'écran pourra observer cet état.
    val uiState: StateFlow<LocationListUiState> = _uiState.asStateFlow()

    init {
        // Au démarrage de l'écran, on lance automatiquement le téléchargement des données.
        loadLocations()
    }

    /**
     * C'est la seule porte d'entrée pour interagir avec le ViewModel.
     */
    fun onAction(action: LocationListUiAction) {
        when (action) {
            is LocationListUiAction.OnLocationClicked -> {
                // Sur Mobile, le changement d'écran physique sera géré par l'objet Navigator côté Compose.
                // Mais on prévient quand même le ViewModel de ce clic, car pour la version Desktop
                // (vue Master-Detail sur un seul écran), le ViewModel devra mémoriser cet ID
                // pour mettre à jour la partie droite de l'interface.
            }
            LocationListUiAction.OnRetryClicked -> {
                // On relance simplement le process de récupération des données.
                loadLocations()
            }
        }
    }

    private fun loadLocations() {
        // On indique à l'UI d'afficher un loader
        _uiState.update { LocationListUiState.Loading }

        viewModelScope.launch {
            val result = repository.getLocations()

            result.fold(
                onSuccess = { locations ->
                    // Si le Repository renvoie les données, on met à jour l'état en Success
                    _uiState.update { LocationListUiState.Success(locations) }
                },
                onFailure = { error ->
                    // S'il y a un problème (ex: pas d'internet et pas de cache), on passe en Error
                    _uiState.update { LocationListUiState.Error(error.message ?: "Erreur inconnue") }
                }
            )
        }
    }
}
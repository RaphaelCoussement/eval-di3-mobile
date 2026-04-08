package presentation.locationdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailViewModel(
    private val locationId: Int, // L'ID cliqué par l'utilisateur
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LocationDetailUiState>(LocationDetailUiState.Loading)
    val uiState: StateFlow<LocationDetailUiState> = _uiState.asStateFlow()

    init {
        loadLocationDetail()
    }

    fun onAction(action: LocationDetailUiAction) {
        when (action) {
            LocationDetailUiAction.OnBackClicked -> {
                // Sera géré par l'UI via le Navigator
            }
            LocationDetailUiAction.OnRetryClicked -> {
                loadLocationDetail()
            }
        }
    }

    private fun loadLocationDetail() {
        _uiState.update { LocationDetailUiState.Loading }

        viewModelScope.launch {
            val result = repository.getLocationById(locationId)

            result.fold(
                onSuccess = { location ->
                    _uiState.update { LocationDetailUiState.Success(location) }
                },
                onFailure = { error ->
                    _uiState.update { LocationDetailUiState.Error(error.message ?: "Erreur") }
                }
            )
        }
    }
}
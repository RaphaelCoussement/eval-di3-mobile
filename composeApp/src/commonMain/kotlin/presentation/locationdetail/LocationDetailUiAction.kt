package presentation.locationdetail

/**
 * Représente toutes les actions que l'utilisateur peut faire sur cet écran.
 */
sealed interface LocationDetailUiAction {
    data object OnBackClicked : LocationDetailUiAction
    data object OnRetryClicked : LocationDetailUiAction
}
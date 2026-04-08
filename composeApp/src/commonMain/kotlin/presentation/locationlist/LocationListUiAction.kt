package presentation.locationlist

/**
 * Représente toutes les actions que l'utilisateur peut faire sur cet écran.
 */
sealed interface LocationListUiAction {
    data class OnLocationClicked(val locationId: Int) : LocationListUiAction

    data object OnRetryClicked : LocationListUiAction
}
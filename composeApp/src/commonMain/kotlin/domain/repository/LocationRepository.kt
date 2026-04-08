package domain.repository

import domain.model.Location

interface LocationRepository {
    // Récupère la liste des locations
    suspend fun getLocations(): Result<List<Location>>

    // Récupère le détail d'une location via son ID
    suspend fun getLocationById(id: Int): Result<Location>
}
package data.repository

import data.dto.toDomain
import data.local.LocationLocalDataSource
import data.remote.LocationRemoteDataSource
import domain.model.Location
import domain.repository.LocationRepository

/**
 * Implémentation du LocationRepository.
 * Démontre un mécanisme de fetch : Lecture Locale -> (Si vide) -> Appel Réseau -> Sauvegarde Locale.
 */
class LocationRepositoryImpl(
    private val remoteDataSource: LocationRemoteDataSource,
    private val localDataSource: LocationLocalDataSource
) : LocationRepository {

    override suspend fun getLocations(): Result<List<Location>> {
        return try {
            // On regarde si les infos sont présentes localement
            val cachedLocations = localDataSource.getAllLocations()

            if (cachedLocations.isNotEmpty()) {
                // Si des données, on les map et on les retourne
                return Result.success(cachedLocations.map { it.toDomain() })
            }

            // Si le cache est vide, on va chercher sur l'API
            val remoteLocations = remoteDataSource.fetchLocations()

            // On sauvegarde la réponse de l'API en local
            localDataSource.saveLocations(remoteLocations)

            // On map les données distantes puis on les retourne
            Result.success(remoteLocations.map { it.toDomain() })

        } catch (e: Exception) {
            // Gestion d'erreur
            Result.failure(e)
        }
    }

    override suspend fun getLocationById(id: Int): Result<Location> {
        return try {
            // On regarde si la location existe en local
            val cachedLocation = localDataSource.getLocationById(id)
            if (cachedLocation != null) {
                return Result.success(cachedLocation.toDomain())
            }

            // Si le cache est vide, on va chercher sur l'API
            val remoteLocation = remoteDataSource.fetchLocationById(id)

            // On sauvegarde la réponse de l'API en local
            localDataSource.saveLocation(remoteLocation)

            // On map la données distante puis on la retourne
            Result.success(remoteLocation.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
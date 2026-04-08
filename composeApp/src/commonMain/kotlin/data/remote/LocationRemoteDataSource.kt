package data.remote

import data.dto.LocationDto

/**
 * Interface liée aux données de location depuis l'API
 * */
interface LocationRemoteDataSource {
    suspend fun fetchLocations(): List<LocationDto>
    suspend fun fetchLocationById(id: Int): LocationDto
}
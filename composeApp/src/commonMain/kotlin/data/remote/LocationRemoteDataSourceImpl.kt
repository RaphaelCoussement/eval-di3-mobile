package data.remote

import data.dto.LocationDto
import data.dto.LocationPageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class LocationRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LocationRemoteDataSource {

    private val baseUrl = "https://rickandmortyapi.com/api/location"

    override suspend fun fetchLocations(): List<LocationDto> {
        // On récupère l'objet paginé et on extrait juste la liste "results"
        val response = httpClient.get(baseUrl).body<LocationPageDto>()
        return response.results
    }

    override suspend fun fetchLocationById(id: Int): LocationDto {
        // On interroge l'API pour une location précise
        return httpClient.get("$baseUrl/$id").body()
    }
}
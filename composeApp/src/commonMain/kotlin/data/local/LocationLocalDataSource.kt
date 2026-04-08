package data.local

import data.dto.LocationDto

/**
 * Classe chargée de récupérer/sauvegarder les données des locations en local
 * */
class LocationLocalDataSource {
    private val cache = mutableMapOf<Int, LocationDto>()

    fun getAllLocations(): List<LocationDto> {
        return cache.values.toList()
    }

    fun getLocationById(id: Int): LocationDto? {
        return cache[id]
    }

    fun saveLocations(locations: List<LocationDto>) {
        locations.forEach { cache[it.id] = it }
    }

    fun saveLocation(location: LocationDto) {
        cache[location.id] = location
    }
}
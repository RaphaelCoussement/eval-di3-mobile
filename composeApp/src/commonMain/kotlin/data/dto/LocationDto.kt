package data.dto

import domain.model.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("dimension") val dimension: String,
    @SerialName("residents") val residents: List<String>, // L'API renvoie une liste d'URLs
    @SerialName("url") val url: String,
    @SerialName("created") val created: String
)

@Serializable
data class LocationPageDto(
    @SerialName("results") val results: List<LocationDto>
)

// Fonction qui permet de faire le mapping entre le dto et le domain (notamment pour le compteur)
fun LocationDto.toDomain(): Location {
    return Location(
        id = this.id,
        name = this.name,
        type = this.type,
        dimension = this.dimension,
        residentCount = this.residents.size
    )
}
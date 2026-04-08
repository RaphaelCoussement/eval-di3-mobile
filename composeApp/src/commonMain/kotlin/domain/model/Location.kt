package domain.model

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentCount: Int // On va juste afficher le nombre de résidents par lieu
)
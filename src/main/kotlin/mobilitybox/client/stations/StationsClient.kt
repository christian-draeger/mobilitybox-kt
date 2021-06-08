package mobilitybox.client.stations

import mobilitybox.client.BaseClient.fetch
import mobilitybox.client.Endpoints

public class StationsClient(
    public val apiVersion: String,
    public val baseUrl: String,
) {

    private val String.url get() = "$baseUrl/$apiVersion$this"

    public fun searchByName(query: String, position: GeoPosition? = null): List<Station> =
        fetch<List<Station>> {
            url = Endpoints.STATIONS_SEARCH_BY_NAME.path.url
            extras = {
                param {
                    "query" to query
                    if (position != null) {
                        "latitude" to position.latitude
                        "longitude" to position.longitude
                    }
                }
            }
        }.orEmpty()

    public fun searchByPosition(position: GeoPosition): List<Station> =
        fetch<List<Station>> {
            url = Endpoints.STATIONS_SEARCH_BY_POSITION.path.url
            extras = {
                param {
                    "latitude" to position.latitude
                    "longitude" to position.longitude
                }
            }
        }.orEmpty()

    public fun searchById(id: String, type: String? = null): List<Station> =
        fetch<List<Station>> {
            url = Endpoints.STATIONS_SEARCH_BY_ID.path.url
            extras = {
                param {
                    "query" to id
                    if (type != null) {
                        "id_type" to type
                    }
                }
            }
        }.orEmpty()

    public data class GeoPosition(
        val latitude: Double,
        val longitude: Double,
    )

    public data class Station(
        val id: String,
        val name: String,
        val position: GeoPosition
    )
}

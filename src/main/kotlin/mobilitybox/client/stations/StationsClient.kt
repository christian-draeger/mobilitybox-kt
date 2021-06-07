package mobilitybox.client.stations

import mobilitybox.client.BaseClient.fetch
import mobilitybox.client.Endpoints
import mobilitybox.client.stations.StationsClientResponse.StationsClientResponseItem

public class StationsClient(
    public val apiVersion: String,
    public val baseUrl: String,
) {

    private val String.url get() = "$baseUrl/$apiVersion$this"

    public fun searchByName(query: String, position: GeoPosition? = null): StationsClientResponse? = fetch {
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
    }

    public fun searchByPosition(position: GeoPosition): StationsClientResponse? = fetch {
        url = Endpoints.STATIONS_SEARCH_BY_POSITION.path.url
        extras = {
            param {
                "latitude" to position.latitude
                "longitude" to position.longitude
            }
        }
    }

    public fun searchById(id: String, type: String? = null): StationsClientResponse? = fetch {
        url = Endpoints.STATIONS_SEARCH_BY_ID.path.url
        extras = {
            param {
                "query" to id
                if (type != null) {
                    "id_type" to type
                }
            }
        }
    }

    public data class GeoPosition(
        val latitude: Double,
        val longitude: Double,
    )
}

public class StationsClientResponse : ArrayList<StationsClientResponseItem>() {
    public data class StationsClientResponseItem(
        val id: String,
        val name: String,
        val position: Position
    ) {
        public data class Position(
            val latitude: Double,
            val longitude: Double
        )
    }
}

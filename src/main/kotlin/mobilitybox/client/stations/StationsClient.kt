package mobilitybox.client.stations

import mobilitybox.MobilityboxConfig
import mobilitybox.MobilityboxDsl
import mobilitybox.client.BaseClient.fetch
import mobilitybox.client.stations.StationsClientResponse.StationsClientResponseItem

public class StationsClient(
    public val apiVersion: String,
    public val baseUrl: String,
) {

    internal val searchByNameEndpoint: String = "/stations/search_by_name.json"
    private val String.url get() = "$baseUrl/$apiVersion$this"

    public fun searchByName(query: String, position: GeoPosition? = null): StationsClientResponse? = fetch {
        url = searchByNameEndpoint.url
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

    internal val searchByPositionEndpoint: String = "/stations/search_by_position.json"

    public fun searchByPosition(position: GeoPosition): StationsClientResponse? = fetch {
        url = searchByPositionEndpoint.url
        extras = {
            param {
                "latitude" to position.latitude
                "longitude" to position.longitude
            }
        }
    }

    internal val searchByIdEndpoint: String = "/stations/search_by_position.json"

    public fun searchById(id: String, type: String? = null): StationsClientResponse? = fetch {
        url = searchByIdEndpoint.url
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

@MobilityboxDsl
public fun <T> MobilityboxConfig.stations(init: StationsClient.() -> T): T =
    StationsClient(apiVersion = apiVersion, baseUrl = baseUrl).init()

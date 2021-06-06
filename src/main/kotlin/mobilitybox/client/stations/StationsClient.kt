package mobilitybox.client.stations

import mobilitybox.MobilityboxConfig
import mobilitybox.MobilityboxDsl
import mobilitybox.client.BaseClient.fetch
import mobilitybox.client.stations.StationsClientResponse.StationsClientResponseItem

public class StationsClient {

    public val searchByNameEndpoint: String = "/stations/search_by_name.json"

    public fun searchByName(query: String, position: GeoPosition? = null): StationsClientResponse? = fetch {
        endpoint = searchByNameEndpoint
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

    public val searchByPositionEndpoint: String = "/stations/search_by_position.json"

    public fun searchByPosition(position: GeoPosition): StationsClientResponse? = fetch {
        endpoint = searchByPositionEndpoint
        extras = {
            param {
                "latitude" to position.latitude
                "longitude" to position.longitude
            }
        }
    }

    public val searchByIdEndpoint: String = "/stations/search_by_position.json"

    public fun searchById(id: String, type: String? = null): StationsClientResponse? = fetch {
        endpoint = searchByIdEndpoint
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
public fun <T> MobilityboxConfig.stations(init: StationsClient.() -> T): T = StationsClient().init()

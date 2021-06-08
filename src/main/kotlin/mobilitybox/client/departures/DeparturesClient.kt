package mobilitybox.client.departures

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import mobilitybox.client.BaseClient.fetch
import mobilitybox.client.Endpoints

internal val now: ULong get() = System.currentTimeMillis().toULong()

public class DeparturesClient(
    public val apiVersion: String,
    public val baseUrl: String,
) {
    private val String.url get() = "$baseUrl/$apiVersion$this"

    public fun get(stationId: String, time: ULong = now, maxDepartures: UInt = 10u): List<Departure> =
        fetch<List<Departure>> {
            url = Endpoints.DEPARTURES.path.url
            extras = {
                param {
                    "station_id" to stationId
                    "time" to time
                    "max_departures" to maxDepartures.toInt()
                }
            }
        }.orEmpty()

    @JsonIgnoreProperties(ignoreUnknown = true)
    public data class Departure(
        val departure: Details,
        val trip: Trip
    ) {
        public data class Details(
            @JsonProperty("scheduled_at")
            val scheduledAt: Long,
            @JsonProperty("predicted_at")
            val predictedAt: Long?,
            val platform: String
        )

        public data class Trip(
            val id: String,
            @JsonProperty("end_station_id")
            val endStationId: String,
            val type: Type,
            @JsonProperty("line_name")
            val lineName: String,
            val provider: String,
            val headsign: String,
        ) {
            public data class Type(
                val kind: String,
                val product: String
            )
        }
    }
}

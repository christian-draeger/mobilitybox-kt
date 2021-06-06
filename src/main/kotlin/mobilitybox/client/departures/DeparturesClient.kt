package mobilitybox.client.departures

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import mobilitybox.MobilityboxConfig
import mobilitybox.MobilityboxDsl
import mobilitybox.client.BaseClient.fetch

internal val now: ULong get() = System.currentTimeMillis().toULong()

public class DeparturesClient {
    public fun get(stationId: String, time: ULong = now, maxDepartures: UInt = 10u): DeparturesClientResponse? = fetch {
        endpoint = "/departures.json"
        extras = {
            param {
                "station_id" to stationId
                "time" to time
                "max_departures" to maxDepartures.toInt()
            }
        }
    }
}

public class DeparturesClientResponse : ArrayList<DeparturesClientResponse.Departure>() {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public data class Departure(
        val departure: DepartureDetails,
        val trip: TripDetails
    ) {
        public data class DepartureDetails(
            @JsonProperty("scheduled_at")
            val scheduledAt: Long,
            @JsonProperty("predicted_at")
            val predictedAt: Long?,
            val platform: String
        )

        public data class TripDetails(
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

@MobilityboxDsl
public fun <T> MobilityboxConfig.departures(init: DeparturesClient.() -> T): T = DeparturesClient().init()

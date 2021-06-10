import mobilitybox.client.departures.DeparturesClient.Departure
import mobilitybox.client.departures.DeparturesClient.Departure.Details
import mobilitybox.client.departures.DeparturesClient.Departure.Trip
import mobilitybox.client.stations.StationsClient.GeoPosition
import mobilitybox.client.stations.StationsClient.Station

internal fun aValidStationsClientResponse(
    id: String = "4711",
    name: String = "some name",
    position: GeoPosition = GeoPosition(0.815, 0.815),
) = listOf(
    Station(
        id = id,
        name = name,
        position = position,
    )
)

internal val aValidDeparturesClientResponse = listOf(
    Departure(
        departure = Details(
            scheduledAt = 123456789, // <--  not serialized to scheduled_at
            predictedAt = null, // <--
            platform = "42"
        ),
        trip = Trip(
            id = "some-id",
            endStationId = "Pankow", // <--
            lineName = "XXX", // <--
            provider = "BVG",
            headsign = "111"
        )
    )
)

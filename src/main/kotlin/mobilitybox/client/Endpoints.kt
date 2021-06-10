package mobilitybox.client

public enum class Endpoints(public val path: String) {
    STATIONS_SEARCH_BY_NAME("/stations/search_by_name.json"),
    STATIONS_SEARCH_BY_POSITION("/stations/search_by_position.json"),
    STATIONS_SEARCH_BY_ID("/stations/search_by_id.json"),
    DEPARTURES("/departures.json"),
}

package mobilitybox.client.stations

import Testcontainer
import aValidStationsClientResponse
import mobilitybox.client.Endpoints
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

val wiremock = Testcontainer.wiremock

internal class StationsClientTest {

    @Test
    internal fun `can get stations by id`() {

        wiremock.setupStub(
            path = Endpoints.STATIONS_SEARCH_BY_ID.path,
            body = aValidStationsClientResponse()
        )

        with(StationsClient(baseUrl = wiremock.httpUrl, apiVersion = "v1")) {
            expectThat(searchById("4711")).isEqualTo(aValidStationsClientResponse())
        }
    }

    @Test
    internal fun `can get stations by name`() {

        wiremock.setupStub(
            path = Endpoints.STATIONS_SEARCH_BY_NAME.path,
            body = aValidStationsClientResponse()
        )

        with(StationsClient(baseUrl = wiremock.httpUrl, apiVersion = "v1")) {
            expectThat(searchByName("foo")).isEqualTo(aValidStationsClientResponse())
        }
    }

    @Test
    internal fun `can get stations by position`() {

        wiremock.setupStub(
            path = Endpoints.STATIONS_SEARCH_BY_POSITION.path,
            body = aValidStationsClientResponse()
        )

        with(StationsClient(baseUrl = wiremock.httpUrl, apiVersion = "v1")) {
            val result = searchByPosition(StationsClient.GeoPosition(.0, .0))
            expectThat(result).isEqualTo(aValidStationsClientResponse())
        }
    }

    @Test
    internal fun `will return empty list on none successful status code`() {
        wiremock.setupStub(
            path = Endpoints.DEPARTURES.path,
            statusCode = 400,
            body = aValidStationsClientResponse()
        )
        with(StationsClient(baseUrl = wiremock.httpUrl, apiVersion = "v1")) {
            val result = searchByPosition(StationsClient.GeoPosition(.0, .0))
            expectThat(result).isEmpty()
        }
    }
}

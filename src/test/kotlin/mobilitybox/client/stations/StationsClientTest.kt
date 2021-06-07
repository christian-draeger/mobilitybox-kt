package mobilitybox.client.stations

import Testcontainer
import mobilitybox.client.Endpoints
import mobilitybox.client.stations.StationsClientResponse.StationsClientResponseItem
import mobilitybox.client.stations.StationsClientResponse.StationsClientResponseItem.Position
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

val wiremock = Testcontainer.wiremock

internal class StationsClientTest {

    private val aValidResponse = StationsClientResponse().apply {
        add(
            StationsClientResponseItem(
                id = "4711",
                name = "some name",
                position = Position(0.815, 0.815),
            )
        )
    }

    @Test
    internal fun `can get stations by id`() {

        wiremock.setupStub(
            path = Endpoints.STATIONS_SEARCH_BY_ID.path,
            body = aValidResponse
        )

        with(StationsClient(baseUrl = wiremock.httpUrl, apiVersion = "v1")) {
            expectThat(searchById("4711")).isEqualTo(aValidResponse)
        }
    }
}

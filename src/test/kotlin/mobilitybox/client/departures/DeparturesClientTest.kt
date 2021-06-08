package mobilitybox.client.departures

import Testcontainer
import aValidDeparturesClientResponse
import mobilitybox.client.Endpoints
import mobilitybox.client.stations.wiremock
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

val wiremock = Testcontainer.wiremock

internal class DeparturesClientTest {

    @Test
    @Disabled("wiremock stub does not serialize custom fields properly")
    internal fun `can get departures`() {

        wiremock.setupStub(
            path = Endpoints.DEPARTURES.path,
            body = aValidDeparturesClientResponse
        )

        with(DeparturesClient(baseUrl = wiremock.httpUrl, apiVersion = "v1")) {
            expectThat(get("some-id")).isEqualTo(aValidDeparturesClientResponse)
        }
    }
}

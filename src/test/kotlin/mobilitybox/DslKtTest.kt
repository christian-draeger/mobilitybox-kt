package mobilitybox

import aValidStationsClientResponse
import mobilitybox.client.Credentials
import mobilitybox.client.Endpoints
import mobilitybox.client.departures.now
import mobilitybox.client.stations.StationsClient.GeoPosition
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThanOrEqualTo

val wiremock = Testcontainer.wiremock

internal class DslKtTest {
    @Test
    internal fun `can create config with defaults via DSL`() {
        val config = mobilitybox { this }
        expectThat(config) {
            get { apiKey }.isEmpty()
            get { apiVersion }.isEqualTo("v1")
            get { baseUrl }.isEqualTo("https://api.themobilitybox.com")
        }
    }

    @Test
    internal fun `can set general config values via DSL`() {
        val config = mobilitybox {
            apiKey = "xxx"
            apiVersion = "42"
            baseUrl = "http://localhost:4711"
            this
        }
        expectThat(config) {
            get { apiKey }.isEqualTo("xxx")
            get { apiVersion }.isEqualTo("42")
            get { baseUrl }.isEqualTo("http://localhost:4711")
        }
    }

    @Test
    @Disabled
    internal fun `will update credentials with given apiKey`() {
        expectThat(Credentials.apiKey).isEqualTo("")
        val someApiKey = "my-api-key"
        mobilitybox {
            apiKey = someApiKey
        }
        expectThat(Credentials.apiKey).isEqualTo(someApiKey)
    }

    @Test
    internal fun `can receive stations by id via dsl`() {

        wiremock.setupStub(
            path = Endpoints.STATIONS_SEARCH_BY_ID.path,
            body = aValidStationsClientResponse(name = "Shangri-La")
        )

        val result = mobilitybox {
            baseUrl = wiremock.httpUrl
            stations {
                searchById("4711")
            }
        }
        expectThat(result[0].name).isEqualTo("Shangri-La")
    }

    @Test
    internal fun `can receive stations by name via dsl`() {

        wiremock.setupStub(
            path = Endpoints.STATIONS_SEARCH_BY_NAME.path,
            body = aValidStationsClientResponse(name = "Atlantis")
        )

        val result = mobilitybox {
            baseUrl = wiremock.httpUrl
            stations {
                searchByName("Atlantis")
            }
        }
        expectThat(result[0].name).isEqualTo("Atlantis")
    }

    @Test
    internal fun `can receive stations by position via dsl`() {

        wiremock.setupStub(
            path = Endpoints.STATIONS_SEARCH_BY_POSITION.path,
            body = aValidStationsClientResponse(name = "Utopia")
        )

        val result = mobilitybox {
            baseUrl = wiremock.httpUrl
            stations {
                searchByPosition(GeoPosition(.0, .0))
            }
        }
        expectThat(result[0].name).isEqualTo("Utopia")
    }

    @Test
    @Disabled("playground to check against real api")
    internal fun `can receive departures via dsl`() {

        val result = mobilitybox {
            apiKey = "xxx"
            departures {
                get("vesputi-station-OW_67U4PIKCwCBxtnNWwZ2jnsNS2WZA1eNY9MyjyvKs")
            }
        }
        expectThat(result[0].departure.scheduledAt).isGreaterThanOrEqualTo(now.toLong())
    }

    @Test
    @Disabled("playground to check against real api")
    internal fun `can receive stations via dsl`() {

        val result = mobilitybox {
            apiKey = "xxx"
            stations {
                searchByName("gransee")
            }
        }
        expectThat(result[0].name).isEqualTo("Gransee, Ausbau")
    }
}

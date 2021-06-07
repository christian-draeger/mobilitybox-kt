package mobilitybox.client

import Testcontainer
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

val httpBin = Testcontainer.httpBin
val wiremock = Testcontainer.wiremock

internal class BaseClientTest {

    @Test
    internal fun `will call any url with apiKey from credential store`() {
        Credentials.apiKey = "foo"
        val response = BaseClient.fetch<HttpBinResponse> {
            url = "$httpBin/anything"
        }
        expectThat(response?.headers?.authorization).isEqualTo("Bearer foo")
    }

    @Test
    @Disabled("succeeds from IDE but not from terminal")
    internal fun `will set session-token if available`() {
        wiremock.setupStub(sessionToken = "some-session-token")

        expectThat(Credentials.session).isNull()

        BaseClient.fetch<HttpBinResponse> {
            url = "${wiremock.httpUrl}/"
        }
        expectThat(Credentials.session).isEqualTo("some-session-token")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class HttpBinResponse(
        val headers: Headers,
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Headers(
            @JsonProperty("Authorization")
            val authorization: String,
        )
    }
}

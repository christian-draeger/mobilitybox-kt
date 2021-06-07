package mobilitybox

import mobilitybox.client.Credentials
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

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
}

package mobilitybox.client

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class CredentialsTest {
    @Test
    internal fun `can set apiKey`() {
        Credentials.apiKey = "foo"
        expectThat(Credentials.apiKey).isEqualTo("foo")
    }

    @Test
    internal fun `can override apiKey`() {
        Credentials.apiKey = "bar"
        expectThat(Credentials.apiKey).isEqualTo("bar")
        Credentials.apiKey = "foobar"
        expectThat(Credentials.apiKey).isEqualTo("foobar")
    }
}

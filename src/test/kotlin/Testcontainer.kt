import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

object Testcontainer {
    data class Wiremock(
        val client: WireMock,
        val httpUrl: String,
        val httpsUrl: String,
    ) {
        fun setupStub(
            path: String = "/",
            statusCode: Int = 200,
            sessionToken: String = "",
            body: Any? = null,
        ): StubMapping = client.register(
            WireMock.get(WireMock.urlMatching(".*$path.*"))
                .willReturn(
                    WireMock.aResponse().withHeader("Content-Type", "application/json; charset=UTF-8")
                        .withStatus(statusCode)
                        .withBody(body?.let { ObjectMapper().writeValueAsString(it) })
                        .withHeader("Session-Token", sessionToken)
                )
        )
    }

    val wiremock: Wiremock by lazy {
        with(WireMockContainer().apply { start() }) {
            Wiremock(
                client = WireMock(containerIpAddress, getMappedPort(httpPort)),
                httpUrl = "http://$containerIpAddress:${getMappedPort(httpPort)}",
                httpsUrl = "https://$containerIpAddress:${getMappedPort(httpsPort)}",
            )
        }
    }

    val httpBin: String by lazy {
        with(HttpBinContainer().apply { start() }) {
            "http://$containerIpAddress:${getMappedPort(internalPort)}"
        }
    }
}

private class WireMockContainer(
    version: String = "2.27.2-alpine",
    val httpPort: Int = 8080,
    val httpsPort: Int = 8443,
) : GenericContainer<WireMockContainer>(DockerImageName.parse("rodolpheche/wiremock:$version")) {
    init {
        withExposedPorts(httpPort, httpsPort)
        withCommand("--https-port $httpsPort")
        waitingFor(Wait.forListeningPort())
    }
}

private class HttpBinContainer(
    version: String = "latest",
    val internalPort: Int = 80,
) : GenericContainer<HttpBinContainer>(DockerImageName.parse("kennethreitz/httpbin:$version")) {
    init {
        withExposedPorts(internalPort)
        waitingFor(Wait.forListeningPort())
    }
}

package mobilitybox

import mobilitybox.client.Credentials
import mobilitybox.client.departures.DeparturesClient
import mobilitybox.client.stations.StationsClient

/**
 * Instantiate a Mobilitybox Config that will add the apiKey to the libraries credential store
 * @return T
 */
@MobilityboxDsl
public fun <T> mobilitybox(init: MobilityboxConfig.() -> T): T = MobilityboxConfig().init()

@MobilityboxDsl
public fun <T> MobilityboxConfig.stations(init: StationsClient.() -> T): T =
    StationsClient(apiVersion = apiVersion, baseUrl = baseUrl).init()

@MobilityboxDsl
public fun <T> MobilityboxConfig.departures(init: DeparturesClient.() -> T): T =
    DeparturesClient(apiVersion = apiVersion, baseUrl = baseUrl).init()

public data class MobilityboxConfig(
    var apiKey: String = "",
    var apiVersion: String = "v1",
    var baseUrl: String = "https://api.themobilitybox.com"
) {
    init {
        Credentials.apiKey = apiKey
    }
}

@DslMarker
public annotation class MobilityboxDsl

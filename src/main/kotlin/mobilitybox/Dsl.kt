package mobilitybox

import mobilitybox.client.Credentials

/**
 * Instantiate a Mobilitybox Config that will add the apiKey to the libraries credential store
 * @return T
 */
@MobilityboxDsl
public fun <T> mobilitybox(init: MobilityboxConfig.() -> T): T = MobilityboxConfig().init()

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

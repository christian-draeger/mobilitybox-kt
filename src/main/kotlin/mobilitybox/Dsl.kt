package mobilitybox

import mobilitybox.client.Credentials

@MobilityboxDsl
public fun <T> mobilitybox(init: MobilityboxConfig.() -> T): T = MobilityboxConfig().init()

public data class MobilityboxConfig(
    var apiKey: String = ""
) {
    init {
        Credentials.apiKey = apiKey
    }
}

@DslMarker
public annotation class MobilityboxDsl

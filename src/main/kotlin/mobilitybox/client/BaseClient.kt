package mobilitybox.client

import com.fasterxml.jackson.core.JsonParseException
import io.github.rybalkinsd.kohttp.dsl.context.HttpGetContext
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.ext.url
import io.github.rybalkinsd.kohttp.jackson.ext.toType
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal const val BASE_URL = "https://api.themobilitybox.com"
internal const val API_VERSION = "v1"

public object BaseClient {
    internal inline fun <reified T : Any> fetch(init: BaseClientBuilder.() -> Unit) =
        with(BaseClientBuilder().also(init)) {
            httpGet {
                url("$BASE_URL/$API_VERSION$endpoint")
                header {
                    "Authorization" to "Bearer ${Credentials.apiKey}"
                    "Accept" to "application/json"

                    Credentials.session?.let {
                        "session-token" to it
                    }
                }
                extras()
            }.also {
                if (it.isSuccessful) {
                    Credentials.session = it.header("session-token")
                }
            }
        }.deserialize<T>()

    internal data class BaseClientBuilder(
        var endpoint: String = "",
        var extras: HttpGetContext.() -> Unit = {}
    )
}

internal inline fun <reified T : Any> Response.deserialize() =
    when {
        isSuccessful -> {
            try {
                toType<T>().also { body()?.close() }
            } catch (e: JsonParseException) {
                println("${T::class.simpleName} - deserialization error ${e.message}")
                body()?.close()
                null
            }
        }
        else -> {
            println("${T::class.simpleName} - fetch error (${code()} - ${message()})")
            body()?.close()
            null
        }
    }

public inline val <reified T> T.logger: Logger
    get() = LoggerFactory.getLogger(T::class.java)

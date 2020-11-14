import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.browser.window

val endpoint = window.location.origin

val client_ = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    followRedirects = true
}

suspend fun findPerson(name: String): Person {
    println("querying...")
    return client_.post("$endpoint/call") {
        contentType(ContentType.Application.Json)
        body=name
    }
}

suspend fun getAge(): String {
    return client_.use { client ->
        client.get("${endpoint}/age")
    }
}
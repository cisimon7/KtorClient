import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.utils.io.core.*
import kotlinx.browser.window

val endpoint = window.location.origin

val httpClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    followRedirects = true
}

suspend fun findPerson(name: String): Person {
    println("querying...")
    val response = runCatching {
        httpClient.post<Person>("$endpoint/call") {
            body=name
        }
    }

    return response.fold(
        { result -> println(result); result },
        { error -> println(error); throw error }
    )
}

suspend fun getAge(): String {
    return httpClient.get("${endpoint}/age")
}
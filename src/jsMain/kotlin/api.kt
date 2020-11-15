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
    val person: Person
    try {
        person = client_.post("$endpoint/call") {
            /*contentType(ContentType.Application.Json)*/
            body=name
        }
    } catch (error: Exception) {
        println(error)
        throw error
    }
    return person
}

suspend fun getAge(): String {
    return client_.get("${endpoint}/age")
}
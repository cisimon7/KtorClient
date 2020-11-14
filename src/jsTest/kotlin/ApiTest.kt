import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.test.Test

private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

val people = mutableListOf(
    Person("Khol Bezeng", 22),
    Person("Naomi Ajibade", 25)
)

internal class ApiTest : CoroutineScope by GlobalScope {

    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (request.url.fullUrl) {
                    "http://localhost/" -> {
                        val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Text.Plain.toString()))
                        respond("Hello, world", headers = responseHeaders)
                    }
                    "http://localhost/age" -> {
                        respond("${27}"/*, headers = responseHeaders*/)
                    }
                    "http://localhost/call" -> {
                        val name = request.body.toString()
                        val person: Person = people.find { it.name.contains(name) } ?: Person("", 0)
                        println(name)
                        respond(person.name/*, headers = responseHeaders*/)
                    }
                    else -> error("Unhandled ${request.url.fullUrl}")
                }
            }
        }
    }

    @Test fun hello() = promise { println(client.get<String>("/")) }

    @Test fun getAge() = promise {
        val age = client.get<String>("/age")
        println(age)
    }

    @Test fun call() = promise {
        val name = client.post<String>("/call") {
            contentType(ContentType.Application.Json)
            body="K"
        }

        println("Found: $name")
    }
}
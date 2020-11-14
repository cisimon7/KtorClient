import io.ktor.application.*
import io.ktor.client.features.*
import io.ktor.features.*
import io.ktor.html.respondHtml
import io.ktor.http.*
import io.ktor.http.ContentType.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.routing
import io.ktor.serialization.*
import kotlinx.html.*
import kotlinx.serialization.json.Json

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/output.js") {}
    }
}

val people = mutableListOf(
        Person("Shinji Hatake", 22),
        Person("Boruto Akimiji", 25)
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) {
            json(
                    contentType = Application.Json,
                    json = Json { prettyPrint = true }
            )
        }
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            get("/age") {
                println("Server-Age")
                call.respondText("${27}", ContentType.Text.Plain)
            }
            post("/call") {
                println("Server-Call")
                val name = call.receiveText()
                val person: Person = people.find { it.name.contains(name) } ?: Person("", 0)
                call.respond(HttpStatusCode.OK, person)
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}
import kotlinext.js.jsObject
import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window
import react.child

fun main() {
    window.onload = {
        render(document.getElementById("root")) {
            child(
                component=app,
                props=jsObject {
                    name = "Seven"
                }
            )
        }
    }
}

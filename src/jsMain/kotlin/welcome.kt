import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.RProps
import react.dom.br
import react.dom.button
import react.dom.div
import react.dom.h3
import react.functionalComponent
import react.useEffect
import react.useState
import styled.css
import styled.styledDiv
import styled.styledInput

val scope_ = MainScope()

val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineException: $exception")
}

val app = functionalComponent<RProps> {
    val (age, setAge) = useState("1")
    val (person, setPerson) = useState("")

    useEffect(dependencies = listOf()) {
        scope_.launch {
            setAge(getAge())
        }
    }

    fun find(name: String) {
        scope_.launch(handler) {
            println("received: ${findPerson(name).name}")
        }
    }

    div {
        styledInput {
            css { +WelcomeStyles.textInput }
            attrs {
                type = InputType.text
                value = person
                onChangeFunction = { event ->
                    val query = (event.target as HTMLInputElement).value
                    setPerson(query)
                }
            }
        }
        button {
            +"Born"
            attrs.onClickFunction = { find(person) }
        }
        br {  }
        h3 { +person }
    }
}

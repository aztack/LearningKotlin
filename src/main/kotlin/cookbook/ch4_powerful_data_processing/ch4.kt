package cookbook.ch4_powerful_data_processing

import code
import comment
import h1
import h2
import list
import not_implemented
import run
import java.lang.IllegalArgumentException
import java.time.Instant
import kotlin.reflect.typeOf

fun ch4() {
    h1("Ch4. Powerful Data Processing")
    h2(arrayOf(
        ::composing_and_consuming_collections_the_easy_way,
        ::filtering_datasets,
        ::automatic_null_removal,
        ::sorting_data_with_custom_comparator,
        ::building_strings_based_on_dataset_elements,
        ::dividing_data_into_subsets,
        ::data_transformation_with_map_and_flatMap,
        ::folding_and_reducing_data_sets,
        ::grouping_data
    ))
}

data class Message(val text: String, val sender: String = "anonymous", val timestamp: Instant = Instant.now())
val sentMessages = listOf(
    Message("Hi Agat, any plans for the evening?", "Samuel"),
    Message("Great, I'll take some wine too", "Samuel")
)
val inboxMessages = mutableListOf(
    Message("Let's go out of town and watch the stars tonight!", "Agat"),
    Message("Excellent!", "Agat")
)
fun composing_and_consuming_collections_the_easy_way() {
    code("""
    val sentMessages = listOf(
        Message("Hi Agat, any plans for the evening?", "Samuel"),
        Message("Great, I'll take some wine too", "Samuel")
    )
    val inboxMessages = mutableListOf(
        Message("Let's go out of town and watch the stars tonight!", "Agat"),
        Message("Excelent!", "Agat")
    )
    val allMessages = sentMessages + inboxMessages
    allMessages.forEach{ (text, _) ->
        println(text)
    }
    """)
    run {
        val allMessages = sentMessages + inboxMessages
        allMessages.forEach { (text, _) ->
            println(text)
        }
    }

    comment("""
    Collection<T> override operator plus/minus. You can union/subtract collection with `+` and `-`    
    """)
}

fun filtering_datasets () {
    code("""
    (sentMessages + inboxMessages).filter { it.sender == "Samuel" }.forEach(::println)
    """)
    run {
        (sentMessages + inboxMessages).filter { it.sender == "Samuel" }.forEach(::println)
    }
    list(arrayOf(
     "`::` in Kotlin is about meta-programming, including method references, property references and class literals. ",
     "[Kotlin Reference: Reflect](https://kotlinlang.org/docs/reference/reflection.html)",
     "[kotlin.reflect](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/)"
    ))
}

data class News(val title: String, val url: String) {
    override fun toString(): String = "[$title]($url)"
}
fun automatic_null_removal () {
    fun getNews() = listOf(
        News("Kotlin 1.4.0 is out!", "https://blog.jetbrains.com/kotlin"),
        News("Google launches Android KTX Kotlin extensions for developers", "http://android-developers.googleblog.com"),
        null,
        null,
        News("How to Pick a Career", "https://waitbutwhy.com")
    )
    code("""
    data class News(val title: String, val url: String) {
        override fun toString(): String = "[${'$'}title](${'$'}url)"
    }
    fun getNews() = listOf(
        News("Kotlin 1.4.0 is out!", "https://blog.jetbrains.com/kotlin"),
        News("Google launches Android KTX Kotlin extensions for developers", "http://android-developers.googleblog.com"),
        null,
        null,
        News("How to Pick a Career", "https://waitbutwhy.com")
    )
    getNews().filterNotNull()
        .forEachIndexed { index, news ->
            println("${'$'}index. ${'$'}news")
        }
    """)
    run {
        getNews().filterNotNull()
            .forEachIndexed { index, news ->
                println("$index. $news")
            }
    }
}

data class Message2(val text: String,
                    val sender: String,
                    val receiver: String,
                    val time: Instant = Instant.now());
fun sorting_data_with_custom_comparator () {
    code("""
    data class Message2(val text: String,
                    val sender: String,
                    val receiver: String,
                    val time: Instant = Instant.now());
    val sentMessages = listOf(
        Message2(
            "I'm programming in Kotlin, of course",
            "Samuel",
            "Agat",
            Instant.parse("2018-12-18T10:13:35Z")
        ),
        Message2(
            "Sure!",
            "Samuel",
            "Agat",
            Instant.parse("2018-12-18T10:15:35Z")
        )
    )
    val inboxMessages = mutableListOf(
        Message2(
            "Hey Sam, any plans for the evening?",
            "Samuel",
            "Agat",
            Instant.parse("2018-12-18T10:12:35Z")
        ),
        Message2(
            "That's cool, can I join you?",
            "Samuel",
            "Agat",
            Instant.parse("2018-12-18T10:14:35Z")
        )
    )
    val allMessages = sentMessages + inboxMessages
    allMessages.sortedBy { it.time }
        .forEach { println(it.text) }
    """)
    run {
        val sentMessages = listOf(
            Message2(
                "I'm programming in Kotlin, of course",
                "Samuel",
                "Agat",
                Instant.parse("2018-12-18T10:13:35Z")
            ),
            Message2(
                "Sure!",
                "Samuel",
                "Agat",
                Instant.parse("2018-12-18T10:15:35Z")
            )
        )
        val inboxMessages = mutableListOf(
            Message2(
                "Hey Sam, any plans for the evening?",
                "Samuel",
                "Agat",
                Instant.parse("2018-12-18T10:12:35Z")
            ),
            Message2(
                "That's cool, can I join you?",
                "Samuel",
                "Agat",
                Instant.parse("2018-12-18T10:14:35Z")
            )
        )
        val allMessages = sentMessages + inboxMessages
        allMessages.sortedBy { it.time }
            .forEach { println(it.text) }
    }
}

fun building_strings_based_on_dataset_elements () {
    not_implemented()
}
fun dividing_data_into_subsets () {
    code("""
    val messages = listOf(
        Message("Any plans for the evening?"),
        Message("Learning Kotlin, of course"),
        Message("I'm going to watch the new Star Wars movie"),
        Message("Would u like to join?"),
        Message("Meh, I don't know"),
        Message("See you later!"),
        Message("I like ketchup"),
        Message("Did you send CFP for Kotlin Conf?"),
        Message("Sure!")
    )
    messages.windowed(4, partialWindows = true, step = 4) {
        it.map { it.text }
    }.run {
        forEachIndexed { index, it ->
            println("Group ${'$'}{index + 1}. ${'$'}it")
        }
    }
    """)
    run {
        val messages = listOf(
            Message("Any plans for the evening?"),
            Message("Learning Kotlin, of course"),
            Message("I'm going to watch the new Star Wars movie"),
            Message("Would u like to join?"),
            Message("Meh, I don't know"),
            Message("See you later!"),
            Message("I like ketchup"),
            Message("Did you send CFP for Kotlin Conf?"),
            Message("Sure!")
        )
        messages.windowed(4, partialWindows = true, step = 4) {
            it.map { it.text }
        }.run {
            forEachIndexed { index, it ->
                println("Group ${index + 1}. $it")
            }
        }
    }
    comment("""
    You can find many useful extension functions for Collections [here](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/)
    """)
}
fun data_transformation_with_map_and_flatMap () {
    not_implemented()
}

data class Track(val title: String, val durationInSeconds: Int)
data class Album(val name: String, val tracks: List<Track>)

fun Album.getStartTime(name: String): Int {
    val index = tracks.indexOfFirst { track -> track.title == name}
    if (index < 0) throw IllegalArgumentException("No such track")
    return tracks.take(index)
        .map {(name, duration) -> duration}
        .fold(0) {acc, i -> acc + i}
}
fun folding_and_reducing_data_sets () {
    code("""
    data class Track(val title: String, val durationInSeconds: Int)
    data class Album(val name: String, val tracks: List<Track>)
    
    fun Album.getStartTime(name: String): Int {
        val index = tracks.indexOfFirst { track -> track.title == name}
        if (index < 0) throw IllegalArgumentException("No such track")
        return tracks.take(index)
            .map {(name, duration) -> duration}
            .fold(0) {acc, i -> acc + i}
    }
    Album(
        "Sunny side up", listOf(
            Track("10/10", 176),
            Track("Coming Up Easy", 292),
            Track("Growing Up Beside You", 191),
            Track("Candy", 303),
            Track("Tricks of the Trade", 151)
        )
    ).run {
        arrayOf("Growing Up Beside You", "Coming Up Easy").forEach {
            println("\"${'$'}it\" started at ${'$'}{getStartTime(it)} seconds")
        }
    }
    """)
    run {
        Album(
            "Sunny side up", listOf(
                Track("10/10", 176),
                Track("Coming Up Easy", 292),
                Track("Growing Up Beside You", 191),
                Track("Candy", 303),
                Track("Tricks of the Trade", 151)
            )
        ).run {
            arrayOf("Growing Up Beside You", "Coming Up Easy").forEach {
                println("\"$it\" started at ${getStartTime(it)} seconds")
            }
        }
    }
    list(arrayOf(
        "`fun <T, R> Iterable<T>.fold(initial: R, operation: (acc: R, T) -> R): R ` is the equivalent of `Array.prototype.reduce` in JavaScipt",
        "`fun <S, T : S> Iterable<T>.reduce(operation: (acc: S, T) -> S): S` is different from JavaScript equivalent. It use first element as initial value"
    ))
}
fun grouping_data () {
    not_implemented()
}
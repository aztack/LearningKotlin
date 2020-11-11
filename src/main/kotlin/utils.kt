fun h1(title: String) {
    println("\n# $title")
}

fun code(code: String, kind: String = "kotlin") {
    println("\n```$kind")
    println(code.replaceIndent(" "))
    println("```\n")
}

fun comment(comment: String) {
    println("\n${comment.replaceIndent("> ")}\n")
}

fun pre(html: String): String {
    return "<pre>$html</pre>"
}

fun h2(functions: Array<() -> Unit>) {
    functions.forEachIndexed { index, fn ->
        print("\n## ${index + 1}. ${fn.toString()
            .replace("function ","")
            .replace(" (Kotlin reflection is not available)","")
            .replace("_".toRegex(), " ").capitalize()}\n")
        fn()
    }
}

fun link(title: String, link: String): String {
    return "[$title]($link)"
}

fun list(items: Array<String>, title: String? = null) {
    if (title != null) println("\n$title:\n")
    items.forEach{println("- $it")}
    println()
}

fun not_implemented() {
    println("\n > not implemented or left blank intentionally")
}

fun run(block: () -> Any?) {
    println("Output:")
    println("\n```")
    block()
    println("\n```")
}

fun html(html: String) {
   println(html)
}

fun text(text: String) {
    println("\n$text\n")
}

fun img(title:String, url: String): String {
    return "![$title]($url)"
}
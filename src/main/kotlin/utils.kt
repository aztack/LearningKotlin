fun h1(title: String) {
    println("\n# $title")
}

fun code(code: String) {
    println("\n```kotlin")
    println(code.replaceIndent(" "))
    println("```\n")
}

fun comment(comment: String) {
    println("\n${comment.replaceIndent("> ")}\n")
}

fun pre(html: String): String {
    return "<pre>$html</pre>"
}

fun h2(funs: Array<() -> Unit>) {
    funs.forEachIndexed { index, fn ->
        print("\n## ${index + 1}. ${fn.toString()
            .replace("function ","")
            .replace(" (Kotlin reflection is not available)","")
            .replace("_".toRegex(), " ").capitalize()}\n")
        fn()
    }
}

fun todo(title: String) {
    print("// TODO: $title")
}

fun link(title: String, link: String): String {
    return "[$title]($link)"
}

fun list(items: Array<String>) {
    items.forEach{it -> println("- $it")}
}

fun not_implemented() {
    println("\n > not implemented")
}

fun run(block: () -> Any) {
    println("Output:")
    println("\n```")
    block()
    println("\n```")
}

fun html(html: String) {
   println(html)
}

fun text(text: String) {
    println(text)
}
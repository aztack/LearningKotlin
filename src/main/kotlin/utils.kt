fun h1(title: String) {
    println("\n# $title")
}

fun code(code: String) {
    println("\n```kotlin")
    println(code.replaceIndent(" "))
    println("```\n")
}

fun comment(comment: String) {
    println("\n> $comment\n")
}

fun h2(funs: Array<() -> Unit>) {
    for (fn in funs) {
        print("\n## ${fn.toString()
            .replace("function ","")
            .replace(" (Kotlin reflection is not available)","")
            .replace("_".toRegex(), " ").capitalize()}\n")
        fn()
    }
}

fun todo(title: String) {
    print("// TODO: $title")
}

fun link(title: String, link: String) {
    println("[$title]($link)")
}

fun list(items: List<String>) {
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
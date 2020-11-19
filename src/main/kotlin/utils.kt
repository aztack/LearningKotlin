import java.io.OutputStream
import java.io.PrintStream


fun h1(title: String) {
    println("\n# $title")
}

fun h2(functions: Array<() -> Unit>) {
    functions.forEachIndexed { index, fn ->
        print("\n## ${index + 1}. ${
            fn.toString()
                    .replace("function ", "")
                    .replace(" (Kotlin reflection is not available)", "")
                    .replace("_".toRegex(), " ").capitalize()
        }\n")
        fn()
    }
}

fun h3(title: String) {
    println("\n### $title")
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

fun img(title: String, url: String): String {
    return "![$title]($url)"
}

class TeeStream(out1: OutputStream, out2: PrintStream) : PrintStream(out1) {
    private var output: PrintStream = out2
    override fun write(buf: ByteArray, off: Int, len: Int) {
        try {
            super.write(buf, off, len)
            output.write(buf, off, len)
        } catch (e: Exception) {
        }
    }

    override fun flush() {
        super.flush()
        output.flush()
    }

}
val origin: PrintStream = System.out
fun tee(outputFile: PrintStream) {
    val teeStdOut: PrintStream = TeeStream(origin, outputFile)
    System.setOut(teeStdOut)
}
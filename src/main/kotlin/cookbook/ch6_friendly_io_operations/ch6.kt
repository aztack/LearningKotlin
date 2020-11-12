package cookbook.ch6_friendly_io_operations

import code
import h1
import h2
import not_implemented
import run
import java.io.File
import java.io.File.separator as SEP

fun ch6() {
    h1("Ch6. Friendly I/O Operations")
    h2(arrayOf(
        ::reading_the_contents_of_a_file,
        ::ensuring_stream_closing_with_the_use_function,
        ::reading_the_contents_of_a_file_line_by_line,
        ::writing_the_contents_to_a_file,
        ::appending_a_file,
        ::easy_file_copying,
        ::traversing_files_in_a_directory
    ))
}

fun reading_the_contents_of_a_file() {
    code("""
    import java.io.File.separator as SEP
    val path = "src/main/resources/file1.txt"
    val filePathName = if (SEP == "/") path else path.replace("/", SEP)
    File(filePathName).run {
        println(readText())
    }
    """)
    run {
        _file1().run {
            println(readText(Charsets.UTF_8))
        }
    }
}
fun ensuring_stream_closing_with_the_use_function() {
    code("""
    val path = "src/main/resources/file1.txt"
    val filePathName = if (SEP == "/") path else path.replace("/", SEP)
    File(filePathName).inputStream().use {
        it.readBytes().also { a: ByteArray -> println(String(a)) }
    }
    """)
    run {
        _file1().inputStream().use {
            it.readBytes().also { a: ByteArray -> println(String(a)) }
        }
    }
}
fun reading_the_contents_of_a_file_line_by_line() {
    code("""
    val path = "src/main/resources/file1.txt"
    val filePathName = if (SEP == "/") path else path.replace("/", SEP)
    File(filePathName).readLines().forEach(::println)
    """)
    run {
        _file1().readLines().forEach(::println)
    }
}
fun writing_the_contents_to_a_file() {
    code("""
    val byAndyHUnt = "..." // omitted for simplicity
    val path = "src/main/resources/temp1.tmp"
    val filePathName = if (SEP == "/") path else path.replace("/", SEP)
    File(filePathName).writeText(byANdyHunt)
    """)
    run {
        _tmpFile("temp1").writeText("""
        No one in the brief history of coputing
        has ever written a piece of perfect softeware.
        It's unlikely that you'll be the first - Andy Hunt
        """.trimIndent())
    }
}
fun appending_a_file() {
    code("""
    val path = "src/main/resources/temp2.tmp"
    val filePathName = if (SEP == "/") path else path.replace("/", SEP)
    with(File(filePathName)) {
        if (exists()) delete()
        appendText(""${'"'}
        A language that doesn't affect the way you think
        about programming
        is worth knowing
        ""${'"'}.trimIndent())
        appendText("\n")
        appendBytes("Alan Perlis".toByteArray())
    }
    """)
    run {
        with(_tmpFile("temp2")) {
            if (exists()) delete()
            appendText("""
            A language that doesn't affect the way you think
            about programming
            is worth knowing
            """.trimIndent())
            appendText("\n")
            appendBytes("Alan Perlis".toByteArray())
        }
    }
}
fun easy_file_copying() {
    not_implemented()
}
fun traversing_files_in_a_directory() {
    code("""
    val path = "src/main/resources/"
    val dir = if (SEP == "/") path else path.replace("/", SEP)
        File(dir).walk()
            .filter { it.isFile }
            .filter { it.extension == "txt" || it.extension == "tmp" }
            .filter { it.length() > 0}
            .forEachIndexed { index, file ->
                println("${'$'}index) ${'$'}{file.path}")
                println("  ${'$'}{file.readText()}")
                println()
            }
    """)
    run {
        _tmpFile("").walk()
            .filter { it.isFile }
            .filter { it.extension == "txt" || it.extension == "tmp" }
            .filter { it.length() > 0}
            .forEachIndexed { index, file ->
                println("$index) ${file.path}")
                println("  ${file.readText()}")
                println()
            }
    }
}

fun _file1(): File {
    val path = "src/main/resources/file1.txt"
    val filePathName = if (SEP == "/") path else path.replace("/", SEP)
    return File(filePathName)
}

fun _tmpFile(name: String): File {
    val path = "src/main/resources/${if (name != "") "$name.tmp" else ""}"
    val filePathName = if (SEP == "/") path else path.replace("/", SEP)
    return File(filePathName)
}
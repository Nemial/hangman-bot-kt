package util

fun escapeMarkdown(text: String): String {
    if (text.isEmpty()) {
        return ""
    }

    val specialChars: CharArray = charArrayOf(
        '*',
        '{',
        '}',
        '=',
        '[',
        ']',
        '(',
        ')',
        '#',
        '_',
        '+',
        '|',
        '-',
        '.',
        '!',
        '~',
        '>',
        '@',
        '\\'
    )

    return text.map { if (specialChars.contains(it)) "\\$it" else it }.joinToString("")
}
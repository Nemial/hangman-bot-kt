package game

import bot.escapeMarkdown


class HangmanGame(private val word: String) {
    private val usedLetters: MutableList<Char> = mutableListOf()
    private val attempts = 6
    private var usedAttempts = 0
    private val hangmanFrames: Map<Int, String> = object : HashMap<Int, String>() {
        init {
            put(
                1, """
                    
                     +---+
                     |   |
                         |
                         |
                         |
                         |
                     =====
                    """.trimIndent()
            )
            put(
                2, """
                      +---+
                      |   |
                      O   |
                          |
                          |
                          |
                      =====
                    """.trimIndent()
            )
            put(
                3, """
                      +---+
                      |   |
                      O   |
                      |   |
                          |
                          |
                      =====
                    """.trimIndent()
            )
            put(
                4, """
                      +---+
                      |   |
                      O   |
                     /|   |
                          |
                          |
                      =====
                    """.trimIndent()
            )
            put(
                5, """
                      +---+
                      |   |
                      O   |
                     /|\  |
                          |
                          |
                      =====
                    """.trimIndent()
            )
            put(
                6, """
                    +---+
                      |   |
                      O   |
                     /|\  |
                     / \  |
                          |
                      =====
                    """.trimIndent()
            )
        }
    }
    private val hangman: String
        get() {
            if (usedAttempts == 0) {
                return ""
            }
            return "```${hangmanFrames[usedAttempts]}```"
        }

    private val guessedWord: String?
        get() = word.map {
            if (usedLetters.contains(it)) it else "_"
        }.joinToString(" ")


    private val remainingAttempts: Int
        get() = attempts - usedAttempts

    val isOver: Boolean
        get() = this.isWon || this.isLost

    private val isWon: Boolean
        get() = word.all {
            usedLetters.contains(it)
        }


    private val isLost: Boolean
        get() = usedAttempts == attempts

    fun guess(letter: Char): String {
        val lowerLetter = letter.lowercaseChar()

        if (usedLetters.contains(lowerLetter)) {
            return escapeMarkdown("Вы уже угадывали эту букву!")
        }

        usedLetters.add(lowerLetter)

        if (lowerLetter !in word) {
            usedAttempts += 1

            if (this.isLost) {
                return escapeMarkdown("Вы проиграли!\nСлово было: $word")
            }

            val content = escapeMarkdown(
                """
                Буквы $letter нет в слове!
                Оставшиеся кол-во попыток: ${this.remainingAttempts}
                Текущее слово: ${this.guessedWord} 
                """.trimIndent()
            )

            return """
               |>${this.hangman}
               |>$content
                """.trimMargin("|>")
        }

        if (this.isWon) {
            return escapeMarkdown("Вы выиграли!\nСлово было: $word")
        }

        val content = escapeMarkdown(
            """
                Буквы $letter есть в слове!
                Оставшиеся кол-во попыток: ${this.remainingAttempts}
                Текущее слово: ${this.guessedWord} 
            """.trimIndent()
        )

        return """
            |>${this.hangman}
            |>$content
            """.trimMargin("|>")
    }
}
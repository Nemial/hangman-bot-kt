package game

interface GameFactory {
    fun createGame(): HangmanGame
}
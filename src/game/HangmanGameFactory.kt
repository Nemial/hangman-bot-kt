package game

class HangmanGameFactory(
    private val wordRepository: WordRepository,
) : GameFactory {
    override fun createGame(): HangmanGame {
        return HangmanGame(wordRepository.getRandomWord())
    }

}
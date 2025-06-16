package repositories

import dev.inmo.tgbotapi.types.IdChatIdentifier
import game.GameFactory
import game.HangmanGame
import io.ktor.util.collections.*

class InMemorySessionRepository(
    private val gameFactory: GameFactory,
) : SessionRepository {
    private val sessions: ConcurrentMap<IdChatIdentifier, HangmanGame> = ConcurrentMap()

    override fun getGame(chatId: IdChatIdentifier): HangmanGame? {
        return sessions[chatId]
    }

    override fun createGame(chatId: IdChatIdentifier) {
        sessions[chatId] = gameFactory.createGame()
    }

    override fun removeGame(chatId: IdChatIdentifier) {
        sessions -= chatId
    }
}
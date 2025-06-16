package repositories

import dev.inmo.tgbotapi.types.IdChatIdentifier
import game.HangmanGame

interface SessionRepository {
    fun getGame(chatId: IdChatIdentifier): HangmanGame?
    fun createGame(chatId: IdChatIdentifier)
    fun removeGame(chatId: IdChatIdentifier)
}
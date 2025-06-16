package handlers

import context.BotContext
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.MarkdownV2
import repositories.SessionRepository

class TextMessageHandler(
    private val sessionRepository: SessionRepository
) : TextHandler {
    override suspend fun handle(context: BotContext) {
        val chatId = context.update.chat.id
        val game = sessionRepository.getGame(chatId)
        if (game == null) {
            context.send(chatId, "Запустите игру, чтобы начать!", HTMLParseMode)
            return
        }

        val text = context.update.content.text

        if (text.length > 1) {
            context.send(chatId, "Введите одну букву!", HTMLParseMode)
            return
        }


        val result = game.guess(text.first())

        context.send(chatId, result, MarkdownV2)
    }
}
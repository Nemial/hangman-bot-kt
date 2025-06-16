package handlers

import context.BotContext
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.utils.PreviewFeature
import repositories.SessionRepository

class StartCommandHandler(
    private val sessionRepository: SessionRepository
) : CommandHandler {
    @OptIn(PreviewFeature::class)
    override suspend fun handle(context: BotContext) {
        val chatId = context.update.chat.id
        sessionRepository.createGame(chatId)
        context.send(chatId, "Игра началась! Угадайте слово", HTMLParseMode)
    }
}
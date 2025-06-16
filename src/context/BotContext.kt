package context

import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.message.ParseMode
import dev.inmo.tgbotapi.types.message.content.TextMessage

data class BotContext(
    val update: TextMessage,
    val send: suspend (ChatIdentifier, String, parseMode: ParseMode?) -> Unit
)

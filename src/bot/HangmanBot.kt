package bot

import context.BotContext
import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import game.HangmanGameFactory
import game.WordRepository
import handlers.StartCommandHandler
import handlers.TextMessageHandler
import org.apache.commons.configuration2.PropertiesConfiguration
import org.slf4j.LoggerFactory
import repositories.InMemorySessionRepository
import kotlin.system.exitProcess

class HangmanBot(
    private val config: PropertiesConfiguration
) {
    private val gameFactory = HangmanGameFactory(WordRepository)
    private val sessionRepository = InMemorySessionRepository(gameFactory)
    private val logger = LoggerFactory.getLogger(HangmanBot::class.java)

    init {
        if (!config.containsKey("TOKEN")) {
            logger.error("Не передан TOKEN для бота!")
            exitProcess(1)
        }
    }

    suspend fun start() {
        val bot = telegramBot(config.getString("TOKEN"))

        bot.buildBehaviourWithLongPolling {
            onCommand("start") {
                StartCommandHandler(sessionRepository).handle(BotContext(it, ::send))
            }

            onText(initialFilter = {
                !it.content.text.startsWith("/")
            }) {
                TextMessageHandler(sessionRepository).handle(BotContext(it, ::send))
            }
        }.join()
    }
}
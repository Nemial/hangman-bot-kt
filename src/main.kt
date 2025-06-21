import bot.HangmanBot
import config.ConfigLoader
import database.DatabaseInitializer
import logging.LoggerConfigurator

suspend fun main() {
    val config = ConfigLoader.load()

    DatabaseInitializer.initialize(config)
    LoggerConfigurator.configure()

    HangmanBot(config).start()
}

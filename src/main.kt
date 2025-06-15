import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import dev.inmo.kslog.common.defaultMessageFormatter
import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.message.MarkdownV2ParseMode
import dev.inmo.tgbotapi.utils.PreviewFeature
import game.HangmanGame
import game.WordRepository
import kotlinx.datetime.Clock
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.MigrationUtils
import table.UserStatTable
import table.UserTable
import table.WordTable
import java.io.File
import kotlin.coroutines.cancellation.CancellationException
import kotlin.system.exitProcess

suspend fun main() {
    val config = loadConfig()

    loadDb(config)
    if (config.getString("APP_ENV") === "dev") {
        genMigrations()
    }

    configureLogger()
    startBot(config)
}

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun genMigrations() {
    val timestamp = Clock.System.now().toEpochMilliseconds()

    transaction {
        MigrationUtils.generateMigrationScript(
            tables = arrayOf(UserTable, UserStatTable, WordTable),
            scriptDirectory = "migrations",
            scriptName = "Version$timestamp"
        )
    }
}


@OptIn(PreviewFeature::class)
suspend fun startBot(config: PropertiesConfiguration) {
    if (!config.containsKey("TOKEN")) {
        println("Не передан TOKEN для бота!")
        exitProcess(1)
    }

    val bot = telegramBot(config.getString("TOKEN"))
    val sessions = mutableMapOf<IdChatIdentifier, HangmanGame>()
    bot.buildBehaviourWithLongPolling {
        onCommand("start") {
            val chatId = it.chat.id
            sessions[chatId] = HangmanGame(WordRepository.getRandomWord())
            send(chatId, "Игра началась! Угадайте слово")
        }

        onText(initialFilter = {
            !it.content.text.startsWith("/")
        }) {
            val chatId = it.chat.id

            if (chatId !in sessions) {
                send(chatId, "Запустите игру, чтобы начать!")
            }

            val game = sessions[chatId]!!

            if (game.isOver) {
                send(chatId, "Запустите игру, чтобы начать!")
            }

            val text = it.content.text

            if (text.length > 1) {
                send(chatId, "Введите одну букву!")
            }

            val result = game.guess(it.content.text[0].lowercaseChar())
            send(chatId, result, parseMode = MarkdownV2ParseMode)
        }
    }.join()
}

fun loadConfig(): PropertiesConfiguration {
    val configuration = Configurations()
    val file = File(".env")
    return configuration.properties(file)
}

fun loadDb(config: PropertiesConfiguration) {
    val dbConfig = HikariConfig().apply {
        jdbcUrl = config.getString("DB_URL")
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = config.getString("DB_USERNAME")
        password = config.getString("DB_PASSWORD")
        maximumPoolSize = 3
        isReadOnly = false
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    val dataSource = HikariDataSource(dbConfig)
    Database.connect(dataSource)
}

fun configureLogger() {
    KSLog.default =
        KSLog { level: LogLevel, tag: String?, message: Any, throwable: Throwable? ->
            if (throwable is CancellationException) return@KSLog
            if (level == LogLevel.ERROR || level == LogLevel.WARNING || level == LogLevel.INFO) {
                println(defaultMessageFormatter(level, tag, message, throwable))
            }
        }
}

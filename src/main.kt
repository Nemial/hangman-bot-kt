import bot.HangmanBot
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import kotlinx.datetime.Clock
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.MigrationUtils
import org.slf4j.LoggerFactory
import tables.UserStatTable
import tables.UserTable
import tables.WordTable
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

suspend fun main() {
    val config = loadConfig()

    loadDb(config)
    if (config.getString("APP_ENV") === "dev") {
        genMigrations()
    }
    configureLogger()

    HangmanBot(config).start()
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

            val logger = LoggerFactory.getLogger(tag ?: "KSLog")
            when (level) {
                LogLevel.ERROR -> logger.error(message.toString(), throwable)
                LogLevel.INFO -> logger.info(message.toString(), throwable)
                LogLevel.DEBUG -> logger.debug(message.toString(), throwable)
                LogLevel.TRACE -> logger.trace(message.toString(), throwable)
                else -> null
            }
        }
}

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import dev.inmo.kslog.common.defaultMessageFormatter
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from
import dev.inmo.tgbotapi.utils.RiskFeature
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.jetbrains.exposed.v1.jdbc.Database
import java.io.File
import kotlin.coroutines.cancellation.CancellationException
import kotlin.system.exitProcess

suspend fun main() {
    val config = loadConfig()

    loadDb(config)
    startBot(config)
}

@OptIn(RiskFeature::class)
suspend fun startBot(config: PropertiesConfiguration) {
    if (!config.containsKey("TOKEN")) {
        println("Не передан TOKEN для бота!")
        exitProcess(1)
    }
    KSLog.default =
        KSLog { level: LogLevel, tag: String?, message: Any, throwable: Throwable? ->
            if (throwable is CancellationException) return@KSLog
            if (level == LogLevel.ERROR || level == LogLevel.WARNING || level == LogLevel.INFO) {
                println(defaultMessageFormatter(level, tag, message, throwable))
            }
        }

    val bot = telegramBot(config.getString("TOKEN"))


    bot.buildBehaviourWithLongPolling {
        onText {
            println(it.from)
        }
    }.join()
}

fun loadConfig(): PropertiesConfiguration {
    val configuration = Configurations()
    val file = File(".env")
    return configuration.properties(file)
}

fun loadDb(config: PropertiesConfiguration) {
    Database.connect(
        url = config.getString("DB_URL"),
        driver = "com.mysql.cj.jdbc.Driver",
        user = config.getString("DB_USERNAME"),
        password = config.getString("DB_PASSWORD"),
    )
}

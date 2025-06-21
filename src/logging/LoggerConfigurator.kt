package logging

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import org.slf4j.LoggerFactory
import kotlin.coroutines.cancellation.CancellationException

object LoggerConfigurator {
    fun configure() {
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
}
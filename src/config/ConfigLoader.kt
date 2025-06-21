package config

import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import java.io.File

object ConfigLoader {
    fun load(): PropertiesConfiguration {
        val configuration = Configurations()
        val file = File(".env")
        return configuration.properties(file)
    }

    fun PropertiesConfiguration.isDevelopment(): Boolean {
        return getString("APP_ENV") == "dev"
    }
}
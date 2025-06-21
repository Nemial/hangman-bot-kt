package database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.commons.configuration2.PropertiesConfiguration
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseInitializer {
    fun initialize(config: PropertiesConfiguration) {
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
}
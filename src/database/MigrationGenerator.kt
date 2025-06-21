package database

import kotlinx.datetime.Clock
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.MigrationUtils
import tables.UserStatTable
import tables.UserTable
import tables.WordTable

object MigrationGenerator {
    @OptIn(ExperimentalDatabaseMigrationApi::class)
    fun generate() {
        val timestamp = Clock.System.now().toEpochMilliseconds()

        transaction {
            MigrationUtils.generateMigrationScript(
                tables = arrayOf(UserTable, UserStatTable, WordTable),
                scriptDirectory = "migrations",
                scriptName = "Version$timestamp"
            )
        }
    }
}
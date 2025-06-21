import config.ConfigLoader
import database.DatabaseInitializer
import database.MigrationGenerator

fun main() {
    val config = ConfigLoader.load()

    DatabaseInitializer.initialize(config)
    MigrationGenerator.generate()
}
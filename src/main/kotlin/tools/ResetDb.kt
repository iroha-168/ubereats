package tools

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun main() {
    val url = "jdbc:postgresql://localhost:5444/ubereats"
    val user = "postgres"
    val password = "postgres"
    val flyway = Flyway.configure()
        .dataSource(url, user, password)
        .locations("filesystem:db/migrations")
        .baselineOnMigrate(true) // Used when migrating an existing database for the first time
        .cleanDisabled(false)
        .load()

    val db = Database.connect(
        url = url,
        driver = "org.postgresql.Driver",
        user = user,
        password = password,
    )

    transaction(db) {
        // This can be commented out to review the generated migration script before applying a migration
        flyway.clean()
    }
}
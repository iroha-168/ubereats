package tools

import com.ubereats.interface_adapter.schema.Discounts
import com.ubereats.interface_adapter.schema.Orders
import com.ubereats.interface_adapter.schema.RestaurantMenuItems
import com.ubereats.interface_adapter.schema.Restaurants
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import java.io.File
import kotlin.time.Clock

// ./gradlew generate --args="init2" で実行する
// init2のところに変更内容を書く　ex. add_email_column
@OptIn(ExperimentalDatabaseMigrationApi::class)
fun main(args: Array<String>) {
    val name = args[0]
    val db = Database.connect(
        "jdbc:postgresql://localhost:5444/ubereats",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
    val tables = arrayOf(
        Restaurants,
        Discounts,
        RestaurantMenuItems,
        Orders,
    )
    val migrations = transaction(db) {
        MigrationUtils.statementsRequiredForDatabaseMigration(*tables)
    }
    if (migrations.isNotEmpty()) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val targetDate = "${now.year}${now.month.number}${now.day}"
        val file = File("./db/migrations")
        val migrationFiles = file.list().toList()
        val version = if (migrationFiles.isEmpty()) {
            1
        } else {
            migrationFiles.size + 1
        }
        transaction(db) {
            MigrationUtils.generateMigrationScript(
                *tables,
                scriptDirectory = "./db/migrations",
                scriptName = "V${version}__$targetDate-$name",
            )
        }
    } else {
        println("no migration necessary")
    }
}
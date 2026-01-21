package interface_adapter.schema

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object Restaurants : Table("restaurant") {
    val id = varchar("id", 36)
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    // TIPS: DateTimeはTimeZone付きだけど、DBに入れるにはUnixTimeにしておきたいからTimeStampを使う
    // TIPS: defaultExpressionはレコードが作られた時だけしか動かない。だからupdatedAtの時は外部からその時点での時刻を入れる必要がある
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
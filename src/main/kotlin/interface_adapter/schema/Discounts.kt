package interface_adapter.schema

import org.jetbrains.exposed.v1.core.Table

object Discounts : Table("discounts") {
    val id = varchar("id", length = 36)
    val restaurantId = reference("restaurant_id", Restaurants.id)
    val name = varchar("name", 255)
    val percentage = float("percentage")
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
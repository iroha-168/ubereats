package interface_adapter.schema

import org.jetbrains.exposed.v1.core.Table

object RestaurantMenuItems : Table("restaurant_menus") {
    val id = varchar("id", 36)
    val restaurantId = varchar("restaurant_id", 36)
    val discountId = varchar("discount_id", 36).nullable()
    val name = varchar("menu", 255)
    val description = varchar("description", 255)
    val price = integer("price")
    val isSoldOut = bool("is_sold_out")
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
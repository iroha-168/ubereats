package interface_adapter.schema

import domain.restaurant.entity.Order
import org.jetbrains.exposed.v1.core.Table

object Orders: Table("orders") {
    val id = varchar("id", 36)
    val restaurantId = reference("restaurant_id", Restaurants.id)
    val customerId = varchar("customer_id", 36)
    val status = enumeration("status", Order.OrderStatus::class).index() // index：検索を早くするためのもの
    // === DeliveryAddressを分解する ===
    val postalCode = varchar("delivery_address", 255)
    val prefecture = varchar("delivery_address", 255)
    val city = varchar("delivery_address", 255)
    val street = varchar("delivery_address", 255)
    val building = varchar("delivery_address", 255)
    // ===
    // totalPriceは、menuIdを紐づけており、menuテーブルから取得できる情報なので、このテーブルには書かない
}

// 中間テーブル（menu-order）
// OrderItemsにquantityを持たせない理由：https://docs.google.com/presentation/d/1K5YWLM7wgeWg7O8xZO2P_SAhSxnmpFrpFBkI5RM2RmU/edit?slide=id.g3b0ffe1786e_0_21#slide=id.g3b0ffe1786e_0_21
object OrderItems: Table("order_items") {
    val id = varchar("id", 36)
    val menuItemId = reference("menu_item_id", RestaurantMenuItems.id)
    val orderId = reference("order_item", Orders.id)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
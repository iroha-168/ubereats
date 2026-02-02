package com.ubereats.interface_adapter.schema

import com.ubereats.domain.restaurant.entity.Order
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object Orders: Table("orders") {
    val id = varchar("id", 36)
    val restaurantId = reference("restaurant_id", Restaurants.id)
    val customerId = varchar("customer_id", 36)
    val status = enumeration("status", Order.OrderStatus::class).index() // index：検索を早くするためのもの
    // === DeliveryAddressを分解する ===
    val postalCode = varchar("postal_code", 255)
    val prefecture = varchar("prefecture", 255)
    val city = varchar("city", 255)
    val street = varchar("street", 255)
    val building = varchar("building", 255)
    // ===
    // totalPriceは、menuIdを紐づけており、menuテーブルから取得できる情報なので、このテーブルには書かない
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

// 中間テーブル（menu-order）
// OrderItemsにquantityを持たせない理由：https://docs.google.com/presentation/d/1K5YWLM7wgeWg7O8xZO2P_SAhSxnmpFrpFBkI5RM2RmU/edit?slide=id.g3b0ffe1786e_0_21#slide=id.g3b0ffe1786e_0_21
object OrderItems: Table("order_items") {
    val id = varchar("id", 36)
    val menuItemId = reference("menu_item_id", RestaurantMenuItems.id)
    val orderId = reference("order_item", Orders.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
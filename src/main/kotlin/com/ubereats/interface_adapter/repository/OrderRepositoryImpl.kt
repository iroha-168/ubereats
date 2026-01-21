package com.ubereats.interface_adapter.repository

import com.ubereats.interface_adapter.schema.OrderItems
import com.ubereats.interface_adapter.schema.Orders
import com.ubereats.interface_adapter.schema.RestaurantMenuItems
import com.ubereats.interface_adapter.schema.Restaurants
import com.ubereats.restaurant.entity.DeliveryAddress
import com.ubereats.restaurant.entity.Order
import com.ubereats.restaurant.entity.OrderId
import com.ubereats.restaurant.entity.OrderItem
import com.ubereats.restaurant.repository.OrderRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OrderRepositoryImpl : OrderRepository {
    override fun findById(
        restaurantId: String,
        orderId: OrderId
    ): Result<Order> = runCatching{
        transaction {
            // OrderテーブルとOrderItemsテーブルをがっちゃんこして、がっちゃんこしたテーブルから、引数に渡されてきたrestaurant IDとorderIdに等しいOrder情報を取得する
            val rows = (Orders innerJoin OrderItems)
                .selectAll()
                .where { (Orders.restaurantId eq restaurantId) and (Orders.id eq orderId.value) }
                .toList()

            val firstRow = rows.first()

            val orderItems = rows.groupBy { it[OrderItems.menuItemId] }.map {
                OrderItem(
                    menuItemId = it.key,
                    quantity = it.value.size,
                )
            }

            Order(
                id = OrderId(firstRow[Orders.id]),
                restaurantId = firstRow[Orders.restaurantId],
                customerId = firstRow[Orders.customerId],
                deliveryAddress = DeliveryAddress(
                    postalCode = firstRow[Orders.postalCode],
                    prefecture = firstRow[Orders.prefecture],
                    city = firstRow[Orders.city],
                    street = firstRow[Orders.street],
                    building = firstRow[Orders.building],
                ),
                orderItems = orderItems,
                status = firstRow[Orders.status],
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun create(order: Order): Result<Unit> = runCatching {
        transaction {
            Orders.insert {
                it[Orders.id] = order.id.value
                it[Orders.status] = order.status
                it[Orders.customerId] = order.customerId
                it[Orders.restaurantId] = order.restaurantId
                it[Orders.city] = order.deliveryAddress.city
                it[Orders.street] = order.deliveryAddress.street
                it[Orders.postalCode] = order.deliveryAddress.postalCode
                it[Orders.prefecture] = order.deliveryAddress.prefecture
                it[Orders.building] = order.deliveryAddress.building
            }
            order.orderItems.forEach { orderItem ->
                repeat(orderItem.quantity) {
                    OrderItems.insert {
                        it[OrderItems.id] = Uuid.random().toString()
                        it[OrderItems.menuItemId] = orderItem.menuItemId
                        it[OrderItems.orderId] = order.id.value
                    }
                }
            }
        }
    }

    override fun update(order: Order): Result<Unit> = runCatching {
        transaction {
            Orders.update({ Orders.id eq order.id.value }) {
                it[Orders.status] = order.status
                it[Orders.customerId] = order.customerId
                it[Orders.restaurantId] = order.restaurantId
                it[Orders.city] = order.deliveryAddress.city
                it[Orders.street] = order.deliveryAddress.street
                it[Orders.postalCode] = order.deliveryAddress.postalCode
                it[Orders.prefecture] = order.deliveryAddress.prefecture
                it[Orders.building] = order.deliveryAddress.building
            }
            order.orderItems.forEach { orderItem ->
                repeat(orderItem.quantity) {
                    OrderItems.update({ OrderItems.orderId eq order.id.value }) {
                        it[OrderItems.menuItemId] = orderItem.menuItemId
                        it[OrderItems.orderId] = order.id.value
                    }
                }
            }
        }
    }

    override fun delete(order: Order): Result<Unit> = runCatching {
        transaction {
            Orders.deleteWhere { Orders.id eq order.id.value }
        }
    }
}

// TIPS : このコードとcompose.ymlでRepository.findByIdの挙動を確認できる（デバッグコード）
// 確認手順：dockerを起動する →  docker compose up -d をターミナルで叩く → com.ubereats.main()をRunする
fun main() {
    Database.connect(
        "jdbc:postgresql://localhost:5444/ubereats",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
    transaction {
        SchemaUtils.drop(Orders, RestaurantMenuItems, OrderItems, Restaurants)
        SchemaUtils.create(Orders, RestaurantMenuItems, OrderItems, Restaurants)

        Restaurants.insert {
            it[Restaurants.id] = "1"
            it[Restaurants.name] = "test"
            it[Restaurants.description] = "test"
            it[Restaurants.createdAt] = Clock.System.now()
            it[Restaurants.updatedAt] = Clock.System.now()
        }

        RestaurantMenuItems.batchInsert(listOf("1", "2", "3")) { id ->
            this[RestaurantMenuItems.id] = id
            this[RestaurantMenuItems.restaurantId] = "1"
            this[RestaurantMenuItems.name] = "test"
            this[RestaurantMenuItems.description] = "test"
            this[RestaurantMenuItems.price] = 1000
            this[RestaurantMenuItems.isSoldOut] = false
        }

        Orders.insert {
            it[Orders.id] = "1"
            it[Orders.restaurantId] = "1"
            it[Orders.customerId] = "1"
            it[Orders.status] = Order.OrderStatus.PREPARING
            it[Orders.postalCode] = "123-4567"
            it[Orders.prefecture] = "tokyo"
            it[Orders.city] = "tokyo"
            it[Orders.street] = "test"
            it[Orders.building] = "1-1-1"
        }

        OrderItems.batchInsert(listOf("1", "1", "2", "1", "2", "3")) { menuItemId ->
            this[OrderItems.menuItemId] = menuItemId
            this[OrderItems.orderId] = "1"
            this[OrderItems.id] = UUID.randomUUID().toString()
        }

        val repo = OrderRepositoryImpl()
        repo.findById("1", OrderId("1"))
            .onSuccess { println(it) }
            .onFailure {
                println(it)
            }
    }
}
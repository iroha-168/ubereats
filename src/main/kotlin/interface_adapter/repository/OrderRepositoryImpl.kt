package interface_adapter.repository

import domain.restaurant.entity.DeliveryAddress
import domain.restaurant.entity.Order
import domain.restaurant.entity.OrderId
import domain.restaurant.entity.OrderItem
import domain.restaurant.repository.OrderRepository
import interface_adapter.schema.OrderItems
import interface_adapter.schema.Orders
import interface_adapter.schema.RestaurantMenuItems
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

class OrderResult(
    val id: String,
    val restaurantId: String,
    val customerId: String,
    val deliveryAddress: DeliveryAddress,
    val status: Order.OrderStatus
)

class OrderItemResult(
    val id: String,
    val menuItemId: String
)

class OrderRepositoryImpl : OrderRepository {
    override fun findById(
        restaurantId: String,
        orderId: OrderId
    ): Result<Order> = runCatching{
        // TODO: OrderテーブルとOrderItemsテーブルをがっちゃんこして、
        // がっちゃんこしたテーブルから、引数に渡されてきたrestaurant IDとorderIdに等しいOrder情報を取得する

        (Orders innerJoin OrderItems)
            .selectAll()
            .where { (Orders.restaurantId eq restaurantId) and (Orders.id eq orderId.value) }
            .groupBy { it[Orders.id] }
            .map {
                val hoge = OrderItems.menuItemId.count()
                it[]
                val orderItems = it.value.map {
                    OrderItem(
                        menuItemId = it[OrderItems.menuItemId],

                    )
                }
            }


        (Orders innerJoin OrderItems)
            .selectAll()
            .where { (Orders.restaurantId eq restaurantId) and (Orders.id eq orderId.value) }
            .map { row ->
                OrderResult(
                    id = row[Orders.id],
                    restaurantId = row[Orders.restaurantId],
                    customerId = row[Orders.customerId],
                    deliveryAddress = DeliveryAddress(
                        postalCode = row[Orders.postalCode],
                        prefecture = row[Orders.prefecture],
                        city = row[Orders.city],
                        street = row[Orders.street],
                        building = row[Orders.building],
                    ),
                    status = row[Orders.status]
                ) to OrderItemResult(
                    id = row[OrderItems.id],
                    menuItemId = row[OrderItems.menuItemId]
                )
            }
            .groupBy { it.first.id }
            .map {
                val orderResult = it.value.first().first
                val orderItemResults = it.value.map { it.second }
                Order(
                    id = OrderId(it.key),
                    restaurantId = orderResult.restaurantId,
                    customerId = orderResult.customerId,
                    deliveryAddress = orderResult.deliveryAddress,
                    status = orderResult.status,
                    orderItems = orderItemResults.groupBy { it.id }.flatMap { result ->
                        result.value.map {
                            OrderItem(
                                menuItemId = it.menuItemId,
                                quantity = result.value.size
                            )
                        }
                    },
                )
            }.first()
    }

    override fun create(order: Order): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun update(order: Order): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun delete(order: Order): Result<Unit> {
        TODO("Not yet implemented")
    }
}
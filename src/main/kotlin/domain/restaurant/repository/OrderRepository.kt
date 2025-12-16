package domain.restaurant.repository

import domain.restaurant.entity.Order
import domain.restaurant.entity.OrderId

interface OrderRepository {
    fun findAll(restaurantId: String): Result<Order>

    fun findById(restaurantId: String, orderId: OrderId): Result<Order>

    fun create(order: Order): Result<Unit>

    fun update(order: Order): Result<Unit>

    fun delete(order: Order): Result<Unit>
}
package domain.restaurant.repository

import domain.restaurant.entity.OrderAggregate
import domain.restaurant.entity.OrderId

interface OrderRepository {
    fun findAll(restaurantId: String): Result<OrderAggregate>

    fun findById(restaurantId: String, orderId: OrderId): Result<OrderAggregate>

    fun create(orderAggregate: OrderAggregate): Result<Unit>

    fun update(orderAggregate: OrderAggregate): Result<Unit>

    fun delete(orderAggregate: OrderAggregate): Result<Unit>
}